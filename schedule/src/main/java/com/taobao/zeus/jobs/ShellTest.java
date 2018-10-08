package com.taobao.zeus.jobs;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ShellTest {
    public static void main(String[] args) throws Exception {

        Map<String, String> envMap = new HashMap<String, String>(System.getenv());

        ProcessBuilder builder = new ProcessBuilder(partitionCommandLine("sh /home/bigdata/tengdajun/zeus/run_job_dir/2017-01-04/1760/1483530616818.sh"));
        builder.directory(new File("/home/bigdata/tengdajun/zeus/run_job_dir"));
        builder.environment().putAll(envMap);
        Process process = builder.start();

        final InputStream inputStream = process.getInputStream();
        final InputStream errorStream = process.getErrorStream();

        try{
            BufferedReader reader=new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while((line=reader.readLine())!=null){
                System.out.println("############output:  " + line);
            }
        }catch(Exception e){
            e.printStackTrace();
        }

        try {
            BufferedReader reader=new BufferedReader(new InputStreamReader(errorStream));
            String line;
            while((line=reader.readLine())!=null){
                System.out.println("############error:  " + line);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String[] partitionCommandLine(String command) {

        ArrayList<String> commands = new ArrayList<String>();

        String os=System.getProperties().getProperty("os.name");
        if(os!=null && (os.startsWith("win") || os.startsWith("Win"))){
            commands.add("CMD.EXE");
            commands.add("/C");
            commands.add(command);
        }else{
            int index = 0;

            StringBuffer buffer = new StringBuffer(command.length());

            boolean isApos = false;
            boolean isQuote = false;
            while(index < command.length()) {
                char c = command.charAt(index);

                switch(c) {
                    case ' ':
                        if(!isQuote && !isApos) {
                            String arg = buffer.toString();
                            buffer = new StringBuffer(command.length() - index);
                            if(arg.length() > 0) {
                                commands.add(arg);
                            }
                        } else {
                            buffer.append(c);
                        }
                        break;
                    case '\'':
                        if(!isQuote) {
                            isApos = !isApos;
                        } else {
                            buffer.append(c);
                        }
                        break;
                    case '"':
                        if(!isApos) {
                            isQuote = !isQuote;
                        } else {
                            buffer.append(c);
                        }
                        break;
                    default:
                        buffer.append(c);
                }

                index++;
            }

            if(buffer.length() > 0) {
                String arg = buffer.toString();
                commands.add(arg);
            }
        }
        return commands.toArray(new String[commands.size()]);
    }
}
