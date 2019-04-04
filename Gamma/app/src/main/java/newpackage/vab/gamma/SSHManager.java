package newpackage.vab.gamma;

import android.util.Log;

import com.jcraft.jsch.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SSHManager
{
    private static final Logger LOGGER =
            Logger.getLogger(SSHManager.class.getName());
    private JSch jschSSHChannel;
    private String strUserName;
    private String strConnectionIP;
    private int intConnectionPort;
    private String strPassword;
    private Session sesConnection;
    private int intTimeOut;



    private void doCommonConstructorActions(String userName,
                                            String password, String connectionIP, String knownHostsFileName)
    {
        jschSSHChannel = new JSch();

        try
        {
            jschSSHChannel.setKnownHosts(knownHostsFileName);
        }
        catch(JSchException jschX)
        {
            logError(jschX.getMessage());
        }

        strUserName = userName;
        strPassword = password;
        strConnectionIP = connectionIP;
    }

    private SSHManager()
    {
        String userName= "shashi";
        String password="ShaShi@456";
        String connectionIP="172.16.26.43";
        String knownHostsFileName="";

        doCommonConstructorActions(userName, password,
                connectionIP, knownHostsFileName);
        intConnectionPort = 22;
        intTimeOut = 60000;

        new Thread(new Runnable() {
            @Override
            public void run() {

                sshInstance.connect();
                //Execute commands here
            }
        }).start();
    }

    public static SSHManager sshInstance=null;

    public static SSHManager getSSHinstance()
    {
        if(sshInstance==null)
         sshInstance= new SSHManager();

        return sshInstance;
    }



    public String connect()
    {
        String errorMessage = null;

        try
        {

            Properties config= new Properties();
            config.put("StrictHostKeyChecking","no");


            sesConnection = jschSSHChannel.getSession(strUserName,
                    strConnectionIP, intConnectionPort);
            sesConnection.setPassword(strPassword);
            // UNCOMMENT THIS FOR TESTING PURPOSES, BUT DO NOT USE IN PRODUCTION
            // sesConnection.setConfig("StrictHostKeyChecking", "no");

            sesConnection.setConfig(config);
            sesConnection.connect(intTimeOut);
        }
        catch(JSchException jschX)
        {
            errorMessage = jschX.getMessage();
        }

        return errorMessage;
    }

    private String logError(String errorMessage)
    {
        if(errorMessage != null)
        {
            LOGGER.log(Level.SEVERE, "{0}:{1} - {2}",
                    new Object[]{strConnectionIP, intConnectionPort, errorMessage});
        }

        return errorMessage;
    }

    private String logWarning(String warnMessage)
    {
        if(warnMessage != null)
        {
            LOGGER.log(Level.WARNING, "{0}:{1} - {2}",
                    new Object[]{strConnectionIP, intConnectionPort, warnMessage});
        }

        return warnMessage;
    }

    public String sendCommand(String command)
    {
        StringBuilder outputBuffer = new StringBuilder();

        try
        {
            Channel channel = sesConnection.openChannel("exec");
            ((ChannelExec)channel).setCommand(command);
            InputStream commandOutput = channel.getInputStream();
            channel.connect();
            int readByte = commandOutput.read();

            while(readByte != 0xffffffff)
            {
                outputBuffer.append((char)readByte);
                readByte = commandOutput.read();
            }

            channel.disconnect();
        }
        catch(IOException ioX)
        {
            logWarning(ioX.getMessage());
            return null;
        }
        catch(JSchException jschX)
        {
            logWarning(jschX.getMessage());
            return null;
        }

        return outputBuffer.toString();
    }

    public void close()
    {
       if(sesConnection!=null)
        sesConnection.disconnect();
    }


    public void executeSSHCommand(final String str)
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                sshInstance.sendCommand(str);
            }
        }).start();


    }




}