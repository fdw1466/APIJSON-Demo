/*Copyright ©2016 TommyLemon(https://github.com/TommyLemon/APIJSON)

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.*/

package apijson.demo.model;

import apijson.MethodAccess;
import apijson.framework.BaseModel;
import apijson.orm.Visitor;

import java.util.List;

import static apijson.orm.AbstractVerifier.ADMIN;
import static apijson.orm.AbstractVerifier.UNKNOWN;

/**
 * 用户信息
 *
 * @author DWER
 */
@MethodAccess(
        POST = {UNKNOWN, ADMIN},
        DELETE = {ADMIN}
)
public class User extends BaseModel implements Visitor<Long> {
    private static final long serialVersionUID = 1L;

    public static final int SEX_MAIL = 0;
    public static final int SEX_FEMALE = 1;
    public static final int SEX_UNKNOWN = 2;

    /**
     * 姓名
     */
    private String name;
    /**
     * 性别
     */
    private Integer sex;
    /**
     * 年龄
     */
    private Integer age;

    /**
     * 默认构造方法，JSON等解析时必须要有
     */
    public User() {
        super();
    }

    public User(long id) {
        this();
        setId(id);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Override
    public List<Long> getContactIdList() {
        return null;
    }
}
