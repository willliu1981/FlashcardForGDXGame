package idv.kuan.testlib.test;

public class Test3 {

    static class MsgManager {

        public void initChain() {


        }

    }


    static class T1 {
        static final Class<?> next = T2.class;


        static public void convert() {

        }


    }

    static class T2 {
        static final Class<?> next = T3.class;


        static public void convert() {

        }
    }

    static class T3 {


        static public void convert() {

        }
    }


    public String test() {

        return null;
    }
}
