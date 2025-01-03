package com.emm.mapper.type;

import com.emm.util.uuid.UUIDTools;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.sql.*;

@Slf4j
@MappedJdbcTypes(JdbcType.BINARY)
@MappedTypes({String.class})
public class BinaryUUIDToStringHandle extends BaseTypeHandler<String> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, String parameter, JdbcType jdbcType) throws SQLException {
        if (parameter != null) {
            log.info(parameter);
            ps.setBytes(i, UUIDTools.getUUIDBytes(parameter));
        } else {
            ps.setNull(i, Types.BINARY);
        }
    }

    @Override
    public String getNullableResult(ResultSet rs, String columnName) throws SQLException {
        byte[] bytes = rs.getBytes(columnName);
        return this.bytesToStringUUID(bytes);
    }

    @Override
    public String getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        byte[] bytes = rs.getBytes(columnIndex);
        return this.bytesToStringUUID(bytes);
    }

    @Override
    public String getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        byte[] bytes = cs.getBytes(columnIndex);
        return this.bytesToStringUUID(bytes);
    }

    private String bytesToStringUUID(byte[] bytes) {
        if (bytes != null) {
            return UUIDTools.getUUIDString(bytes);
        } else {
            return null;
        }
    }
}
