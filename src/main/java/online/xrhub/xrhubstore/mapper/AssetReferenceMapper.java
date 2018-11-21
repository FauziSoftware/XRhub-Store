package online.xrhub.xrhubstore.mapper;

import online.xrhub.xrhubstore.entity.AssetReference;
import online.xrhub.xrhubstore.entity.AssetReferenceExample;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface AssetReferenceMapper {
    long countByExample(AssetReferenceExample example);

    int deleteByExample(AssetReferenceExample example);

    @Delete({
        "delete from asset_reference",
        "where Id = #{id,jdbcType=VARCHAR}"
    })
    int deleteByPrimaryKey(String id);

    @Insert({
        "insert into asset_reference (Id, header_id, data_id, ",
        "meta_id)",
        "values (#{id,jdbcType=VARCHAR}, #{headerId,jdbcType=VARCHAR}, ",
        "#{dataId,jdbcType=VARCHAR}, #{metaId,jdbcType=VARCHAR})"
    })
    @SelectKey(statement="SELECT UUID_SHORT()", keyProperty="id", before=true, resultType=String.class)
    int insert(AssetReference record);

    int insertSelective(AssetReference record);

    List<AssetReference> selectByExample(AssetReferenceExample example);

    @Select({
        "select",
        "Id, header_id, data_id, meta_id",
        "from asset_reference",
        "where Id = #{id,jdbcType=VARCHAR}"
    })
    @ResultMap("online.xrhub.xrhubstore.mapper.AssetReferenceMapper.BaseResultMap")
    AssetReference selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") AssetReference record, @Param("example") AssetReferenceExample example);

    int updateByExample(@Param("record") AssetReference record, @Param("example") AssetReferenceExample example);

    int updateByPrimaryKeySelective(AssetReference record);

    @Update({
        "update asset_reference",
        "set header_id = #{headerId,jdbcType=VARCHAR},",
          "data_id = #{dataId,jdbcType=VARCHAR},",
          "meta_id = #{metaId,jdbcType=VARCHAR}",
        "where Id = #{id,jdbcType=VARCHAR}"
    })
    int updateByPrimaryKey(AssetReference record);

    @Select({
            "select",
            "Id, header_id, data_id, meta_id",
            "from asset_reference",
            "where header_id = #{headerId}"
    })
    @ResultMap("online.xrhub.xrhubstore.mapper.AssetReferenceMapper.BaseResultMap")
    List<AssetReference> selectByHeaderId(String headerId);
}