package idv.kuan.testlib.test;

public class Test2 {


    public static class T1 extends T2 {


        private String testMsg;


        public String getTestMsg() {
            return testMsg;
        }

        public void setTestMsg(String testMsg) {
            this.testMsg = testMsg;
        }

        @Override
        public void convert() {
            super.setData(new MyData(this.testMsg));

        }
    }

    public static class T2 extends T3 {
        private MyData data;

        public MyData getData() {
            return data;
        }

        public void setData(MyData data) {
            this.data = data;
        }

        @Override
        public void convert() {
            Msg msg = new Msg();
            msg.setMsgStr("m1");
            msg.setMyData(this.data);
            setMsg(msg);
        }

        public void setMsg2() {
            setMsg2(new Msg());
        }
    }

    public static class T3 implements Convertible {
        private Msg msg;
        private Msg msg2;

        public static class Msg {
            private String msgStr;
            private MyData myData;

            public String getMsgStr() {
                return msgStr;
            }

            public void setMsgStr(String msgStr) {
                this.msgStr = msgStr;
            }

            public MyData getMyData() {
                return myData;
            }

            public void setMyData(MyData myData) {
                this.myData = myData;
            }

            public String outputCombine() {
                return this.myData + ":" + msgStr;
            }


        }

        public Msg getMsg() {
            return msg;
        }

        public void setMsg(Msg msg) {
            this.msg = msg;
        }

        public Msg getMsg2() {
            return msg2;
        }

        public void setMsg2(Msg msg2) {
            this.msg2 = msg2;
        }

        @Override
        public void convert() {

        }
    }


    public String testTest() {

        T1 t1 = new T1();
        t1.setTestMsg("t1 ");
        t1.convert();


        T2 t2 = new T1();
        t2.setData(new MyData("d1"));

        T2 t21 = t1;

        T3.Msg msg = new T3.Msg();
        msg.setMsgStr("M1");
        msg.setMyData(new MyData("T3d"));
        T3 t3 = new T3();
        t3.setMsg(msg);

        T3 t31 = t21;
        t21.convert();


        t21.setMsg2();

        System.out.println("xxx Test2:" + t21.getMsg());
        System.out.println("xxx Test2:" + t31.msg);
        System.out.println("xxx Test2:" + t21.getMsg2());

        return t31.msg.outputCombine();
        // return t21.getData().getData();

    }

}

class MyData {
    private String data;

    public MyData(String data) {
        this.data = data;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "MyData{" +
                "data='" + data + '\'' +
                '}';
    }
}


interface Convertible {


    void convert();
}
