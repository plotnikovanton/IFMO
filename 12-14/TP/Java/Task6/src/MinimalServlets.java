import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MinimalServlets {
    private static Map<String, Entity> entities = new HashMap<>(); //Data set, will erase after restart
    private static int idCounter = 0; //Id counter

    /**
     * Entry point to start server
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        Server server = new Server(8080);
        ServletHandler handler = new ServletHandler();
        server.setHandler(handler);
        handler.addServletWithMapping(Squares.class, "/squares/*");
        server.start();
        server.join();
    }

    /**
     * Main servlet class
     */
    @SuppressWarnings("serial")
    public static class Squares extends HttpServlet {

        /**
         * GET requests
         * @param request
         * @param response
         * @throws ServletException
         * @throws IOException
         */
        @Override
        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            String[] pathSplit = request.getPathInfo() == null? new String[0] : request.getPathInfo().split("/");
            if (pathSplit.length<2) { //If we call "squares" or "squares/"
                //It can be static, but i'm lazy
                String page = "﻿<!DOCTYPE html>\n" +
                        "<html>\n" +
                        " <head>\n" +
                        "   <meta http-equiv=\"Content-Language\" content=\"ru\">\n" +
                        "   <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n" +
                        "   <style>\n" +
                        "     #field {\n" +
                        "       height: 100%;\n" +
                        "       width: 100%;\n" +
                        "       position: fixed;\n" +
                        "       margin: 0;\n" +
                        "       padding: 0;\n" +
                        "       border: solid black thin;\n" +
                        "     }\n" +
                        "   </style>\n" +
                        " </head>\n" +
                        " <body>\n" +
                        "   <div id=\"field\">\n";


                for (Entity e : entities.values()) {
                    page+=e; //Simply call toString() for all Squares and add to page content
                }

                //End of page, close all tags
                page += "   </div>\n" +
                        " </body>\n" +
                        "</html>";

                response.setContentType("text/html;charset=utf-8");
                response.getWriter().println(page);
                response.setStatus(HttpServletResponse.SC_OK);

            } else if (pathSplit.length==2&&entities.containsKey(pathSplit[1])) { //If we want to show info about square(squares/ID)
                Entity e = entities.get(pathSplit[1]);//Find entity in data set
                //Print info about that entity
                response.getWriter().println("Square "+ e.id +"\n" +
                        "x: "+e.x+"\n" +
                        "y: "+e.y+"\n" +
                        "size: "+e.size+"\n" +
                        "color: "+e.color+"\n");
                response.setStatus(HttpServletResponse.SC_OK);
            } else { //Else request is bad
                response.getWriter().println("Bad request");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        }

        /**
         * POST requests, uses only to create new entity
         * @param request
         * @param response
         * @throws ServletException
         * @throws IOException
         */
        @Override
        protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
            Map<String, String[]> params = request.getParameterMap(); //Read params
            boolean isOkay = true; //Flag to watch errors
            int x=0, y=0, size=0;
            String color=null; //Should newer be null

            //Parse and check params, comments not needed
            try { //Parse x
                x = Integer.parseInt(params.get("x")[0]);
            } catch (NumberFormatException e) {
                response.getWriter().println("Wrong x param: "+params.get("x")[0]);
                isOkay = false;
            } catch (NullPointerException e) {
                response.getWriter().println("You should set \"x\" param");
                isOkay = false;
            }

            try { //Parse y
                y = Integer.parseInt(params.get("y")[0]);
            } catch (NumberFormatException e) {
                response.getWriter().println("Wrong y param: "+params.get("y")[0]);
                isOkay = false;
            } catch (NullPointerException e) {
                response.getWriter().println("You should set \"y\" param");
                isOkay = false;
            }

            try { //Parse size
                size = Integer.parseInt(params.get("size")[0]);
            } catch (NumberFormatException e) {
                response.getWriter().println("Wrong size param: "+params.get("size")[0]);
                isOkay = false;
            } catch (NullPointerException e) {
                response.getWriter().println("You should set \"size\" param");
                isOkay = false;
            }

            try { //Parse color
                color = params.get("color")[0];
                //TODO regex to check valid
            } catch (NullPointerException e) {
                response.getWriter().println("You should set \"color\" param");
                isOkay = false;
            }

            if (isOkay) { //If everything was fine
                System.out.println(x+" "+y+" "+size); //debug output
                entities.put(Integer.toString(idCounter), new Entity(x, y, size, color)); //Add new entity to data set
                response.setStatus(HttpServletResponse.SC_CREATED); //OK!
            } else { //Else request was bad, do nothing
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST); //That just happen
            }
        }

        /**
         * PUT request, using to change entities
         * @param request
         * @param response
         * @throws ServletException
         * @throws IOException
         */
        @Override
        protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            String[] pathSplit = request.getPathInfo() == null? new String[0] : request.getPathInfo().split("/"); //Split url
            if (pathSplit.length==2&&entities.containsKey(pathSplit[1])) { //If we'v got something like "squares/ID"
                                                                           // and square with current ID exist
                Map<String, String[]> params = request.getParameterMap(); //Read params

                //Pars params and change in data set if it valid
                if (params.containsKey("x")) { //For x
                    try {
                        entities.get(pathSplit[1]).x = Integer.parseInt(params.get("x")[0]);
                    } catch (NumberFormatException e) {response.getWriter().println("Wrong x param: "+params.get("x")[0]);}
                }
                if (params.containsKey("y")) { //For y
                    try {
                        entities.get(pathSplit[1]).y = Integer.parseInt(params.get("y")[0]);
                    } catch (NumberFormatException e) {response.getWriter().println("Wrong y param: "+params.get("y")[0]);}
                }
                if (params.containsKey("size")) { //For size
                    try {
                        entities.get(pathSplit[1]).size = Integer.parseInt(params.get("size")[0]);
                    } catch (NumberFormatException e) {response.getWriter().println("Wrong y param: "+params.get("size")[0]);}
                }
                if (params.containsKey("color")) { //For color
                    entities.get(pathSplit[1]).color = params.get("color")[0];
                }
                response.setStatus(HttpServletResponse.SC_OK);
            } else { //If square with current ID was not find
                response.getWriter().println("Square not found"); //Alert about it
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        }

        /**
         * DELETE request, using to delete entity
         * @param request
         * @param response
         * @throws ServletException
         * @throws IOException
         */
        @Override
        protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            String[] pathSplit = request.getPathInfo() == null? new String[0] : request.getPathInfo().split("/"); //Split input
            if (pathSplit.length == 2 && entities.containsKey(pathSplit[1])) { //If we'v got something like "squares/ID"
                                                                               // and square with current ID exist
                entities.remove(pathSplit[1]); //Delete entity
                response.setStatus(HttpServletResponse.SC_OK); //Fine!
            } else { //If not exist
                response.getWriter().println("Square not found"); //Alert
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        }
    }

    /**
     * Entity class, contains info about one entity
     */
    public static class Entity{
        int x;
        int y;
        int size;
        String color;
        int id; //ID

        public Entity(int x, int y, int size, String color) {
            this.x = x;
            this.y = y;
            this.size = size;
            this.color = color;
            this.id = idCounter++; //Increase ID counter after entity create
        }

        /**
         *
         * @return <div> block to paint square
         */
        @Override
        public String toString() {
            String result = "<div style=\"" +
                    "position: absolute; " +
                    "width: "+size+"px;" +
                    "height: "+size+"px;" +
                    "background-color: "+color+";" +
                    "left: "+x+"px;" +
                    "top: "+y+"px; \">" +

                    id +

                    "</div>\n";

            return result;
        }
    }
}