package org.dlminer.nn.poc;

/**
 * Created by slava on 10/09/17.
 */
public class UnitFactory {

    private Class<? extends Unit> unitClass;

    public UnitFactory(Class<? extends Unit> unitClass) {
        this.unitClass = unitClass;
    }


    public Unit newInstance() {
        try {
            return unitClass.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
