package exec;

import exec.service.CallExec;
import exec.service.CallRshellThread;

/**
 * �����������
 */
public class Call {
    /**
     * ����ִ��
     *
     * @param code
     * @return
     */
    public String eval(String code) {
        return new CallExec().eval(code);
    }

    /**
     * ����ִ��
     *
     * @param cmd
     * @return
     */
    public String execmd(String cmd) {
        return new CallExec().execmd(cmd);
    }

    /**
     * ����shell
     *
     * @param host
     * @param port
     */
    public void rShell(String host, int port) {
        //return new CallExec().rShell(host, port);
        //���̷߳�ʽ����
        Thread t1 = new Thread(new CallRshellThread(host,port));
        t1.start();
    }
}
