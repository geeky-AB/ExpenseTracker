package Model;

public class Data {
    private int amount;
    private String type;
    private String comment;
    private String id;
    private String date;
    private String dateInt;

    public Data(int amount, String type, String comment, String id, String date, String dateInt) {
        this.amount = amount;
        this.type = type;
        this.comment = comment;
        this.id = id;
        this.date = date;
        this.dateInt = dateInt;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDateInt() {
        return dateInt;
    }

    public void setDateInt(String dateInt) {
        this.dateInt = dateInt;
    }

    public Data(){

    }
}
