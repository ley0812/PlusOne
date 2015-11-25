package ac.plusone.main;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by MinJeong on 2015-11-17.
 */
public class CommentVO implements Parcelable{
    private String num_comment, content, writer, writer_id, date;

    public CommentVO() {

    }

    protected CommentVO(Parcel in) {
        num_comment = in.readString();
        content = in.readString();
        writer = in.readString();
        writer_id = in.readString();
        date = in.readString();
    }

    public static final Creator<CommentVO> CREATOR = new Creator<CommentVO>() {
        @Override
        public CommentVO createFromParcel(Parcel in) {
            return new CommentVO(in);
        }

        @Override
        public CommentVO[] newArray(int size) {
            return new CommentVO[size];
        }
    };

    public String getNum_comment() {
        return num_comment;
    }

    public void setNum_comment(String num_comment) {
        this.num_comment = num_comment;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getWriter_id() {
        return writer_id;
    }

    public void setWriter_id(String writer_id) {
        this.writer_id = writer_id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(num_comment);
        dest.writeString(content);
        dest.writeString(writer);
        dest.writeString(writer_id);
        dest.writeString(date);
    }

    @Override
    public String toString() {
        return "CommentVO{" +
                "num_comment='" + num_comment + '\'' +
                ", content='" + content + '\'' +
                ", writer='" + writer + '\'' +
                ", writer_id='" + writer_id + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
