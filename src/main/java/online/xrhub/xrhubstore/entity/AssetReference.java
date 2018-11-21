package online.xrhub.xrhubstore.entity;

import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author 
 */
@NoArgsConstructor
public class AssetReference implements Serializable {
    private String id;

    private String headerId;

    private String dataId;

    private String metaId;

    private static final long serialVersionUID = 1L;

    public AssetReference(String headerId, String dataId, String metaId) {
        this.headerId = headerId;
        this.dataId = dataId;
        this.metaId = metaId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHeaderId() {
        return headerId;
    }

    public void setHeaderId(String headerId) {
        this.headerId = headerId;
    }

    public String getDataId() {
        return dataId;
    }

    public void setDataId(String dataId) {
        this.dataId = dataId;
    }

    public String getMetaId() {
        return metaId;
    }

    public void setMetaId(String metaId) {
        this.metaId = metaId;
    }
}