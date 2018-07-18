package itx.examples.jetty.common.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ConversationId {

    private String userId;
    private String groupId;

    @JsonCreator
    public ConversationId(@JsonProperty("userId") String userId,
                          @JsonProperty("groupId") String groupId) {
        this.userId = userId;
        this.groupId = groupId;
    }

    public String getUserId() {
        return userId;
    }

    public String getGroupId() {
        return groupId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ConversationId that = (ConversationId) o;

        if (!userId.equals(that.userId)) return false;
        return groupId.equals(that.groupId);
    }

    @Override
    public int hashCode() {
        int result = userId.hashCode();
        result = 31 * result + groupId.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "ConversationId{" +
                "userId='" + userId + '\'' +
                ", groupId='" + groupId + '\'' +
                '}';
    }

}
