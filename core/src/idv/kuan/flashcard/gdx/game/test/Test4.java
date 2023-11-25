package idv.kuan.flashcard.gdx.game.test;

import idv.kuan.libs.interfaces.chain.Convertible;
import idv.kuan.libs.interfaces.chain.VersionConverter;

public class Test4 {


    static class T1ToT2Converter implements VersionConverter {
        private VersionConverter next;

        @Override
        public void setNext(VersionConverter converter) {
            this.next = converter;
        }

        @Override
        public Convertible convert(Convertible input) {
            if (input instanceof TV1) {
                TV2 result = new TV2();
                // ...转换T1到T2的逻辑...
                result.setData(new TV2.TV2Data("data1=" + ((TV1) input).getData(), "name T2"));

                if (next != null) {
                    return next.convert(result);
                } else {
                    return result;
                }
            } else {
                return next != null ? next.convert(input) : input;
            }
        }


    }

    static class T2ToT3Converter implements VersionConverter {
        private VersionConverter next;

        @Override
        public void setNext(VersionConverter converter) {
            this.next = converter;
        }

        @Override
        public Convertible convert(Convertible input) {
            if (input instanceof TV2) {
                TV3 result = new TV3(-1);
                // ...转换T2到T3的逻辑...
                TV2.TV2Data data = ((TV2) input).getData();
                String s = data.toString();
                System.out.println("xxx Test4:tv3 str=" + s);


                int count = 0;
                for (char c : s.toCharArray()) {
                    if (c == (char) 'x') {
                        count++;
                    }
                }


                result.setIntData(count);

                if (next != null) {
                    return next.convert(result);
                } else {
                    return result;
                }
            } else {
                return next != null ? next.convert(input) : input;
            }
        }


    }

    class T3ToT4Converter implements VersionConverter {
        private VersionConverter next;

        @Override
        public void setNext(VersionConverter converter) {
            this.next = converter;
        }

        @Override
        public Convertible convert(Convertible input) {
            if (input instanceof TV3) {
                TV4 result = new TV4("this is convert to tv4", null);
                // ...转换T3到T4的逻辑...
                Integer intData = ((TV3) input).intData;
                TV4.FinalData data = new TV4.FinalData();
                data.setData(new TV2.TV2Data("char count=" + intData, "name TV4"));
                result.setData(data);

                return result;
            } else {
                return next != null ? next.convert(input) : input;
            }
        }
    }


    static class TV1 implements idv.kuan.libs.interfaces.chain.Convertible {
        String data;

        public TV1(String data) {
            this.data = data;
        }

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }
    }

    static class TV2 implements idv.kuan.libs.interfaces.chain.Convertible {
        private TV2Data data;

        public TV2Data getData() {
            return data;
        }

        public void setData(TV2Data data) {
            this.data = data;
        }

        //inner data
        static class TV2Data {
            String innerDataStr;
            String name;

            public TV2Data(String innerDataStr, String name) {
                this.innerDataStr = innerDataStr;
                this.name = name;
            }


            public String getInnerData() {
                return innerDataStr;
            }

            public void setInnerDataStr(String innerDataStr) {
                this.innerDataStr = innerDataStr;
            }

            @Override
            public String toString() {
                return "TV2Data{" +
                        "innerDataStr='" + innerDataStr + '\'' +
                        ", name='" + name + '\'' +
                        '}';
            }
        }

    }

    static class TV3 implements idv.kuan.libs.interfaces.chain.Convertible {

        private Integer intData;

        public TV3(Integer intData) {
            this.intData = intData;
        }

        public Integer getIntData() {
            return intData;
        }

        public void setIntData(Integer intData) {
            this.intData = intData;
        }

        @Override
        public String toString() {
            return "TV3{" +
                    "intData=" + intData +
                    '}';
        }
    }

    static class TV4 implements idv.kuan.libs.interfaces.chain.Convertible {
        String msg;
        FinalData data;


        public TV4(String msg, FinalData data) {
            this.msg = msg;
            this.data = data;
        }

        public void setData(FinalData data) {
            this.data = data;
        }

        static class FinalData {
            TV2.TV2Data data;

            public TV2.TV2Data getData() {
                return data;
            }

            public void setData(TV2.TV2Data data) {
                this.data = data;
            }

            @Override
            public String toString() {
                return "FinalData{" +
                        "data=" + data +
                        '}';
            }
        }

        @Override
        public String toString() {
            return "TV4{" +
                    "msg='" + msg + '\'' +
                    ", data=" + data +
                    '}';
        }
    }

    public String test() {
        VersionConverter converter1 = new T1ToT2Converter();
        VersionConverter converter2 = new T2ToT3Converter();
        VersionConverter converter3 = new T3ToT4Converter();

        converter1.setNext(converter2);
        converter2.setNext(converter3);

        TV1 tv1 = new TV1("this is tv1 xxx");
        TV2 tv2 = new TV2();
        tv2.setData(new TV2.TV2Data("this is tv2 xxxx", "not converted"));


        TV3 tv3 = new TV3(1);
        tv3.setIntData(5);

        TV4.FinalData data = new TV4.FinalData();
        data.setData(new TV2.TV2Data("default count=:0", "Def"));
        TV4 tv4 = new TV4("msg 4", data);


        //TV4 tv41 = (TV4) converter1.convert(tv1);
        //TV2 tv21 = (TV2) converter1.convert(tv2);
        //TV3 tv31 = (TV3) converter1.convert(tv3);
        TV4 tv41 = (TV4) converter1.convert(tv4);


        return tv41.toString();
    }

}
