package com.fabian.vilo.models.CDModels;

import io.realm.RealmObject;

/**
 * Created by Fabian on 12/10/15.
 */
public class CDPoll extends RealmObject {

    private String answer;
    private int pollid;
    private int postid;
    private CDPost post;

    /**
     * Getter
     */
    public String getAnswer() {
        return answer;
    }

    public int getPollid() {
        return pollid;
    }

    public int getPostid() {
        return postid;
    }

    public CDPost getPost() {
        return post;
    }

    /**
     * Setter
     */

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public void setPollid(int pollid) {
        this.pollid = pollid;
    }

    public void setPostid(int postid) {
        this.postid = postid;
    }

    public void setPost(CDPost post) {
        this.post = post;
    }
}
