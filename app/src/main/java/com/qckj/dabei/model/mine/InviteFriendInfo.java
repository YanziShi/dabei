package com.qckj.dabei.model.mine;

import com.qckj.dabei.util.json.JsonField;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangzhizhong on 2019/5/31.
 */
public class InviteFriendInfo {

    public class FriendInfo{
        String member_grade_name;

        String profit;

        String user_code;

        //1:直接用户 2:间接用户
        int flag;

        public String getGradeName() {
            return member_grade_name;
        }

        public void setGradeName(String gradeName) {
            this.member_grade_name = gradeName;
        }

        public String getProfit() {
            return profit;
        }

        public void setProfit(String profit) {
            this.profit = profit;
        }

        public String getAccount() {
            return user_code;
        }

        public void setAccount(String account) {
            this.user_code = account;
        }

        public int getType() {
            return flag;
        }

        public void setType(int type) {
            this.flag = type;
        }
    }

    String count;
    ArrayList<FriendInfo> list;

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public ArrayList<FriendInfo> getList() {
        return list;
    }

    public void setList(ArrayList<FriendInfo> list) {
        this.list = list;
    }
}
