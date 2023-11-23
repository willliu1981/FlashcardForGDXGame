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
                TV3 result = new TV3();
                // ...转换T2到T3的逻辑...
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
                TV4 result = new TV4();
                // ...转换T3到T4的逻辑...
                return result;
            } else {
                return next != null ? next.convert(input) : input;
            }
        }
    }


    static class TV1 implements idv.kuan.libs.interfaces.chain.Convertible {

    }

    static class TV2 implements idv.kuan.libs.interfaces.chain.Convertible {

    }

    static class TV3 implements idv.kuan.libs.interfaces.chain.Convertible {

    }

    static class TV4 implements idv.kuan.libs.interfaces.chain.Convertible {

    }

}
