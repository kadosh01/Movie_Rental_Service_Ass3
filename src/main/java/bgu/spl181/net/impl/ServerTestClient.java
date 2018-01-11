package bgu.spl181.net.impl;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Joseph on 11/01/2018.
 */
public class ServerTestClient {

    private static final String Testpath="Database/tset.json";

    private static List<List<String>> DesirializeTest(){
        List<List<String>> users=new LinkedList<>();
        ArrayList<JsonArray> list;
        try(Reader reader=new FileReader(Testpath)){
            Gson gson=new Gson();
            JsonObject obj=gson.fromJson(reader,JsonObject.class);
            list=gson.fromJson(obj.getAsJsonArray("Commands"),new TypeToken<ArrayList<JsonArray>>(){}.getType());

            char c='"';
            for (JsonArray str :list) {
                List<String> user=new LinkedList<>();
                for(JsonElement s :str){
                    user.add(s.getAsString().replace("'",c+""));
                }
                users.add(user);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return users;
    }
    public static void main(String[] args) throws IOException {
        List<List<String>> users=DesirializeTest();
        for(int j=0;j<users.size();j++){
            int finalJ = j;
            Thread t=new Thread(()->{

                List<String> commands=users.get(finalJ);
                //BufferedReader and BufferedWriter automatically using UTF-8 encoding
                try {
                    try (Socket sock = new Socket("127.0.0.1", 7777);
                         BufferedReader in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
                         BufferedWriter out = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()))) {
                        String line="";
                        int i=0;
                        do {
                            if(!line.contains("BROADCAST")) {
                                if (i < commands.size()) {
                                    System.out.println("sending message to server :" + commands.get(i));
                                    out.write(commands.get(i++));
                                } else break;
                                out.newLine();
                                out.flush();
                            }

                            System.out.println("awaiting response");
                            line = in.readLine();
                            System.out.println("message from server: " + line);
                            if(line.contains("BROADCAST")){

                            }
                        }while(!line.contains("SIGNOUT"));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            });
            t.start();
        }

    }
}
