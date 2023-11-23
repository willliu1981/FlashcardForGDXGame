package idv.kuan.flashcard.gdx.game.test;

public class Test1 {


    //data
    public static class MsgData {

        String dataMsg;

        public String getDataMsg() {
            return dataMsg;
        }

        public void setDataMsg(String dataMsg) {
            this.dataMsg = dataMsg;
        }
    }

    public interface Convertible {

        void convert();
    }


    public static class AEntity implements Convertible {
        String data;

        public void setData(String data) {
            this.data = data;
        }

        public String getData() {
            return data;
        }

        public String getMsg() {

            return this.data;
        }

        @Override
        public void convert() {
            throw new UnsupportedOperationException("未定義方法");
        }
    }

    public static class BEntity implements Convertible {
        AEntity aEntity;
        MsgData msgData;

        public BEntity() {
        }

        public BEntity(AEntity aEntity) {
            this.aEntity = aEntity;
            this.convert();
        }

        @Override
        public void convert() {
            this.msgData = new MsgData();
            this.msgData.setDataMsg(aEntity.data);
        }

        public MsgData getMsgData() {
            return msgData;
        }

        public void setMsgData(MsgData msgData) {
            this.msgData = msgData;
        }


    }

    public static class CEntity implements Convertible {
        BEntity bEntity;
        MsgData msgData;
        String detail;

        public CEntity() {
        }

        public CEntity(BEntity bEntity) {
            this.bEntity = bEntity;
            this.convert();
        }

        @Override
        public void convert() {
            String dataMsg = bEntity.getMsgData().getDataMsg();
            this.msgData = new MsgData();
            this.msgData.setDataMsg("(" + dataMsg + ")");

        }

        public MsgData getMsgData() {
            return msgData;
        }

        public void setMsgData(MsgData msgData) {
            this.msgData = msgData;
        }

        public String getDetail() {
            return detail;
        }

        public void setDetail(String detail) {
            this.detail = detail;
        }

        public String getCombineMsg() {
            return this.msgData.getDataMsg() + ":" + detail;

        }
    }


    public String test2() {

        AEntity aEntity = new AEntity();
        aEntity.setData("test 2");

        BEntity bEntity = new BEntity(aEntity);


        MsgData msgData = new MsgData();
        msgData.setDataMsg("msg 3");
        //bEntity.setMsgData(msgData);


        String text1 = bEntity.getMsgData().getDataMsg();

        CEntity cEntity = new CEntity(bEntity);
        //cEntity.setMsgData(msgData);


        cEntity.setDetail("detail 2");

        text1 = cEntity.getCombineMsg();
        return text1;


    }

}
