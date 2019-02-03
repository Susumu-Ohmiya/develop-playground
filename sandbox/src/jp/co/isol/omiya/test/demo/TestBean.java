package jp.co.isol.omiya.test.demo;

public class TestBean implements Cloneable {

    private String name;
    private int age;
    /**
     * @return name
     */
    public String getName() {
        return name;
    }
    /**
     * @param name セットする name
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * @return age
     */
    public int getAge() {
        return age;
    }
    /**
     * @param age セットする age
     */
    public void setAge(int age) {
        this.age = age;
    }
    
    @Override
    public Object clone() {
        TestBean c = new TestBean();
        c.setName(name);
        c.setAge(age);
        return c;
    }
    
}
