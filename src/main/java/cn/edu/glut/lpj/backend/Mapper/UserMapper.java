package cn.edu.glut.lpj.backend.Mapper;

import cn.edu.glut.lpj.backend.pojo.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
    //
    @Select("select id,nickname,pswd,isAdministrator from users where id = #{user_id};")
    public User authUserInfo(String user_id);

//    @Select("select id,nickname,pswd,isAdministrator from users;")
//    public void connectionText();

    @Select("select id,nickname,pswd,isAdministrator from users where id = #{id};")
    public User getUserById(String id);

    @Insert("insert into users(id,nickname,pswd,isAdministrator) values (#{id},#{nickname},#{pswd},0)")
    public void insertUser(String id, String nickname, String pswd);
}
