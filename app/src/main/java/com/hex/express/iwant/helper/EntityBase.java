
package com.hex.express.iwant.helper;

/**
 * Author: john Date: 14-11-04
 */
public abstract class EntityBase {

    // @Id // 如果主键没有命名名为id或_id的时，需要为主键添加此注解
    // @NoAutoIncrement // int,long类型的id默认自增，不想使用自增时添加此注解
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
