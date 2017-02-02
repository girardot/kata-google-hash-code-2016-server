package google.hash.code;

import google.hash.code.input.InputReader;
import google.hash.code.model.World;
import google.hash.code.score.OutputFileReader;
import google.hash.code.score.ScoreDrone;
import google.hash.code.score.ScoreProcessor;
import org.eclipse.jetty.util.MultiMap;
import org.eclipse.jetty.util.UrlEncoded;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.ModelAndView;
import spark.Request;
import spark.template.freemarker.FreeMarkerEngine;

import javax.servlet.MultipartConfigElement;
import javax.servlet.http.Part;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static spark.Spark.*;

public class Server {

    private static final String SESSION_TEAM_NAME_FIELD = "teamName";
    private static final Logger LOGGER = LoggerFactory.getLogger(Server.class);

    public static void main(String[] args) {
        final ScoreProcessor scoreProcessor = new ScoreProcessor();
        final InputReader inputReader = new InputReader();
        final OutputFileReader outputFileReader = new OutputFileReader();
        final Map<String, TeamScore> scores = new HashMap<>();

        port(5000);
        staticFiles.location("/public");

        get("/", (request, response) -> {
            Map<String, Object> attributes = new HashMap<>();
            attributes.put("scoresByTeam", scores.values());
            return new ModelAndView(attributes, "home.ftl");
        }, new FreeMarkerEngine());

        get("/upload", (request, response) -> {
            Map<String, Object> attributes = new HashMap<>();
            attributes.put("teamName", getTeamNameFromSession(request));
            return new ModelAndView(attributes, "upload.ftl");
        }, new FreeMarkerEngine());

        get("/team", (request, response) -> {
            Map<String, Object> attributes = new HashMap<>();
            attributes.put("teamName", getTeamNameFromSession(request));
            return new ModelAndView(attributes, "team.ftl");
        }, new FreeMarkerEngine());

        post("/team", (request, response) -> {
            MultiMap<String> params = new MultiMap<>();
            final String teamName = extractTeamNameFromParam(request, params);

            File directory = new File("out/" + teamName);
            directory.mkdir();

            request.session(true);
            request.session().attribute(SESSION_TEAM_NAME_FIELD, teamName);
            response.redirect("/");
            return "OK";
        });

        post("/upload/:inputType", "multipart/form-data", (request, response) -> {

            String teamName = getTeamNameFromSession(request);
            InputType inputType = InputType.valueOf(request.params("inputType").toUpperCase());

            String location = "out/" + teamName + "/";          // the directory location where files will be stored
            long maxFileSize = 100000000;       // the maximum size allowed for uploaded files
            long maxRequestSize = 100000000;    // the maximum size allowed for multipart/form-data requests
            int fileSizeThreshold = 1024;       // the size threshold after which files will be written to disk

            MultipartConfigElement multipartConfigElement = new MultipartConfigElement(
                    "/temp",
                    maxFileSize,
                    maxRequestSize,
                    fileSizeThreshold
            );
            request.raw().setAttribute("org.eclipse.jetty.multipartConfig", multipartConfigElement);

            Collection<Part> parts = request.raw().getParts();
            for (Part part : parts) {
                LOGGER.info("Name: " + part.getName());
                LOGGER.info("Size: " + part.getSize());
                LOGGER.info("Filename: " + part.getSubmittedFileName());
            }

            String fName = request.raw().getPart(inputType.label).getSubmittedFileName();
            LOGGER.info("Title: " + request.raw().getParameter("title"));
            LOGGER.info("File: " + fName);

            Part uploadedFile = request.raw().getPart(inputType.label);
            Path out = Paths.get(location + inputType.label);
            try (final InputStream in = uploadedFile.getInputStream()) {
                Files.copy(in, out, REPLACE_EXISTING);
                uploadedFile.delete();
            }

            multipartConfigElement = null;
            parts = null;
            uploadedFile = null;

            final World world = inputReader.parse("/" + inputType.label + ".in");
            final List<ScoreDrone> drones = outputFileReader.parse(out, world);

            final TeamScore teamScore = scores.getOrDefault(teamName, new TeamScore(teamName));
            teamScore.addScore(new Score(inputType, scoreProcessor.computeScore(world, drones)));
            scores.putIfAbsent(teamName, teamScore);

            response.redirect("/");
            return "OK";
        });

        exception(Exception.class, (e, request, response) -> {
            LOGGER.error(request.body());
            LOGGER.error(e.getMessage());
            response.status(500);
        });

        before((request, response) -> {
            LOGGER.info("request {}", request.pathInfo());
            if (!request.pathInfo().contains("/team") && getTeamNameFromSession(request) == null) {
                LOGGER.info("Unknown Team Name");
                response.redirect("/team");
            }
        });

    }

    private static String extractTeamNameFromParam(Request request, MultiMap<String> params) {
        UrlEncoded.decodeTo(request.body(), params, "UTF-8");
        final String teamName = params.get("teamName").get(0);
        return teamName.replace(" ", "_").trim();
    }

    private static String getTeamNameFromSession(Request request) {
        return request.session().attribute(SESSION_TEAM_NAME_FIELD);
    }

}
