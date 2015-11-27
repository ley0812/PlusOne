package ac.plusone.main;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.SimpleFormatter;

/**
 * Created by MinJeong on 2015-11-12.
 */
public class BoardVO implements Parcelable {
    private String title, writer, writer_id, content, category, date;
    private int num;

    public BoardVO() {

    }

    protected BoardVO(Parcel in) {
        title = in.readString();
        writer = in.readString();
        writer_id = in.readString();
        content = in.readString();
        category = in.readString();
        date = in.readString();
        num = in.readInt();
    }

    public static final Creator<BoardVO> CREATOR = new Creator<BoardVO>() {
        @Override
        public BoardVO createFromParcel(Parcel in) {
            return new BoardVO(in);
        }

        @Override
        public BoardVO[] newArray(int size) {
            return new BoardVO[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDate() {
        return date;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public void setDate(String strDate) {

        this.date = strDate;
        //SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd HH:mm");
        //this.date = format.format(strDate);
    }

    public String getWriter_id() {
        return writer_id;
    }

    public void setWriter_id(String writer_id) {
        this.writer_id = writer_id;
    }

    @Override
    public String toString() {
        return "BoardVO{" +
                "title='" + title + '\'' +
                ", writer='" + writer + '\'' +
                ", writer_id='" + writer_id + '\'' +
                ", content='" + content + '\'' +
                ", category='" + category + '\'' +
                ", date='" + date + '\'' +
                ", num=" + num +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(writer);
        dest.writeString(writer_id);
        dest.writeString(content);
        dest.writeString(category);
        dest.writeString(date);
        dest.writeInt(num);
    }
}
