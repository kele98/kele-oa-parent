//
//
package top.aikele.vo.system;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * <p>
 * 角色查询实体
 * </p>
 *
 * @author qy
 * @since 2019-11-08
 */
@ApiModel("角色查询实体")
public class SysRoleQueryVo implements Serializable {
	
	private static final long serialVersionUID = 1L;
	@ApiModelProperty("查询角色名")
	private String roleName;

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
}

