package online.xrhub.xrhubstore.service;

import online.xrhub.xrhubstore.entity.AssetReference;
import online.xrhub.xrhubstore.entity.AssetReferenceExample;
import online.xrhub.xrhubstore.mapper.AssetReferenceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @AUTHOR soft
 * @DATE 2018/11/12 23:46
 * @DESCRIBE
 */
@Service
public class AssetReferenceService {

    private final AssetReferenceMapper mapper;

    @Autowired
    public AssetReferenceService(AssetReferenceMapper mapper) {
        this.mapper = mapper;
    }

    public AssetReference add(AssetReference reference) {
        int i = mapper.insertSelective(reference);
        if (i > 0) {
            return reference;
        }
        return null;
    }

    public boolean deleteByHeaderId(String headerId) {
        AssetReferenceExample example = new AssetReferenceExample();
        example.createCriteria().andHeaderIdEqualTo(headerId);
        return mapper.deleteByExample(example) > 0;
    }

    public AssetReference delete(String id) {
        AssetReference one = getOne(id);
        if (one != null) {
            if (mapper.deleteByPrimaryKey(id) > 0) {
                return one;
            }
        }
        return null;
    }

    public boolean changeByHeaderId(String headerId, AssetReference reference) {
        AssetReferenceExample example = new AssetReferenceExample();
        example.createCriteria().andHeaderIdEqualTo(headerId);
        return mapper.updateByExampleSelective(reference, example) > 0;
    }

    /**
     * 判断元数据是不是被引用
     * @param metaId 元数据id
     * @return true false
     */
    public boolean metaIsUsed(String metaId) {
        AssetReferenceExample example = new AssetReferenceExample();
        AssetReferenceExample.Criteria criteria = example.createCriteria();
        criteria.andMetaIdEqualTo(metaId);
        return mapper.countByExample(example) > 0;
    }

    public List<AssetReference> byHeaderId(String headerId) {
        return mapper.selectByHeaderId(headerId);
    }

    public AssetReference getOne(String id) {
        return mapper.selectByPrimaryKey(id);
    }
}
