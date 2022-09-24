package top.yudoge.pojos;

import lombok.Data;

/**
 * 用户快照
 */
@Data
public class UserSnap {
    private Long id;
    private String nick;
    private String email;

    public static UserSnap fromUser(User user) {
        UserSnap snap = new UserSnap();
        snap.setId(user.getId());
        snap.setNick(user.getNick());
        snap.setEmail(user.getEmail());
        return snap;
    }
}
