/**
 * 
 */
package com.schic.schie.modules.common.Vo;

import com.schic.schie.modules.database.entity.ExDb;
import com.schic.schie.modules.nodes.entity.ExNode;

import java.io.Serializable;
import java.util.List;

public class NodeDbVo implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private List<ExNode> exNodes;
    private List<ExDb> exDbs;
    private ExNode exNode;
    private ExDb exDb;

    public ExNode getExNode() {
        return exNode;
    }

    public void setExNode(ExNode exNode) {
        this.exNode = exNode;
    }

    public ExDb getExDb() {
        return exDb;
    }

    public void setExDb(ExDb exDb) {
        this.exDb = exDb;
    }

    public List<ExNode> getExNodes() {
        return exNodes;
    }

    public void setExNodes(List<ExNode> exNodes) {
        this.exNodes = exNodes;
    }

    public List<ExDb> getExDbs() {
        return exDbs;
    }

    public void setExDbs(List<ExDb> exDbs) {
        this.exDbs = exDbs;
    }
}
