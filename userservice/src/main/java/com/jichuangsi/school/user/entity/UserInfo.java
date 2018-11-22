package com.jichuangsi.school.user.entity;

//import javax.persistence.Id;
/**
 *@NoArgsConstructor: 自动生成无参数构造函数。
  @AllArgsConstructor: 自动生成全参数构造函数。
  @Data: 自动为所有字段添加@ToString, @EqualsAndHashCode, @Getter方法，
         为非final字段添加@Setter,和@RequiredArgsConstructor
 */
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "school_user")
public class UserInfo {
	@Id
	private String id;
	private String userId;
	private String userNum;
	private String userName;
	private String pwd;
	private String classId;
	private String flag;
	private long createTime;
	private long updateTime;

	public UserInfo() {
	}

	public UserInfo(String userId, String userNum, String userName, String pwd, String classId, String flag) {
		this.userId = userId;
		this.userNum = userNum;
		this.userName = userName;
		this.pwd = pwd;
		this.classId = classId;
		this.flag = flag;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserNum() {
		return userNum;
	}

	public void setUserNum(String userNum) {
		this.userNum = userNum;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getClassId() {
		return classId;
	}

	public void setClassId(String classId) {
		this.classId = classId;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}

	@Override
	public String toString() {
		return "UserInfo{" +
				"userId='" + userId + '\'' +
				", userNum='" + userNum + '\'' +
				", userName='" + userName + '\'' +
				", pwd='" + pwd + '\'' +
				", classId='" + classId + '\'' +
				", flag='" + flag + '\'' +
				'}';
	}
}
