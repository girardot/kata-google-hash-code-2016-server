package google.hash.code;

import google.hash.code.input.InputReader;
import google.hash.code.model.World;
import google.hash.code.score.OutputFileReader;
import google.hash.code.score.ScoreDrone;
import google.hash.code.score.ScoreProcessor;
import spark.ModelAndView;
import spark.template.freemarker.FreeMarkerEngine;

import javax.servlet.MultipartConfigElement;
import javax.servlet.http.Part;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static google.hash.code.InputType.SIMPLE;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static spark.Spark.*;

public class Server {

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
            attributes.put("teamName", "team A");
            return new ModelAndView(attributes, "upload.ftl");
        }, new FreeMarkerEngine());


        post("/upload", "multipart/form-data", (request, response) -> {

            String teamName = "team A";
            String location = "out/" + teamName + "-";          // the directory location where files will be stored
            long maxFileSize = 100000000;       // the maximum size allowed for uploaded files
            long maxRequestSize = 100000000;    // the maximum size allowed for multipart/form-data requests
            int fileSizeThreshold = 1024;       // the size threshold after which files will be written to disk

            MultipartConfigElement multipartConfigElement = new MultipartConfigElement(
                    location,
                    maxFileSize,
                    maxRequestSize,
                    fileSizeThreshold
            );
            request.raw().setAttribute("org.eclipse.jetty.multipartConfig", multipartConfigElement);

            Collection<Part> parts = request.raw().getParts();
            for (Part part : parts) {
                System.out.println("Name: " + part.getName());
                System.out.println("Size: " + part.getSize());
                System.out.println("Filename: " + part.getSubmittedFileName());
            }

            String fName = request.raw().getPart("simple").getSubmittedFileName();
            System.out.println("Title: " + request.raw().getParameter("title"));
            System.out.println("File: " + fName);

            Part uploadedFile = request.raw().getPart("simple");
            Path out = Paths.get(location + fName);
            try (final InputStream in = uploadedFile.getInputStream()) {
                Files.copy(in, out, REPLACE_EXISTING);
                uploadedFile.delete();
            }

            multipartConfigElement = null;
            parts = null;
            uploadedFile = null;

            final World world = inputReader.parse("/simple.in");
            final List<ScoreDrone> drones = outputFileReader.parse(out, world);

            final TeamScore teamScore = scores.getOrDefault(teamName, new TeamScore(teamName));
            teamScore.addScore(new Score(SIMPLE, scoreProcessor.computeScore(world, drones)));
            scores.putIfAbsent(teamName, teamScore);

            response.redirect("/");
            return "OK";
        });

    }

}
