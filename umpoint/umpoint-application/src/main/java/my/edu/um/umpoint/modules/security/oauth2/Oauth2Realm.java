package my.edu.um.umpoint.modules.security.oauth2;

import my.edu.um.umpoint.common.exception.ErrorCode;
import my.edu.um.umpoint.common.utils.ConvertUtils;
import my.edu.um.umpoint.common.utils.MessageUtils;
import my.edu.um.umpoint.modules.client.entity.CliTokenEntity;
import my.edu.um.umpoint.modules.client.entity.CliUserEntity;
import my.edu.um.umpoint.modules.security.entity.SysUserTokenEntity;
import my.edu.um.umpoint.modules.security.service.ShiroService;
import my.edu.um.umpoint.modules.security.user.UserDetail;
import my.edu.um.umpoint.modules.sys.entity.SysUserEntity;
import lombok.AllArgsConstructor;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Set;

@Component
@AllArgsConstructor
public class Oauth2Realm extends AuthorizingRealm {
    private final ShiroService shiroService;

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof Oauth2Token;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        UserDetail user = (UserDetail) principals.getPrimaryPrincipal();

        Set<String> permsSet = shiroService.getUserPermissions(user);

        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.setStringPermissions(permsSet);
        return info;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String accessToken = (String) token.getPrincipal();

        SysUserTokenEntity tokenEntity = shiroService.getByToken(accessToken);
        CliTokenEntity cliTokenEntity = shiroService.getCliByToken(accessToken);
        if (
            (tokenEntity == null && cliTokenEntity == null) ||
            (tokenEntity != null && tokenEntity.getExpireDate().getTime() < System.currentTimeMillis()) ||
            (cliTokenEntity != null && cliTokenEntity.getExpireDate().getTime() < System.currentTimeMillis())
        ) {
            throw new IncorrectCredentialsException(MessageUtils.getMessage(ErrorCode.TOKEN_INVALID));
        }

        UserDetail userDetail;
        if (tokenEntity != null) {
            SysUserEntity userEntity = shiroService.getUser(tokenEntity.getUserId());
            userDetail = ConvertUtils.sourceToTarget(userEntity, UserDetail.class);
            List<Long> deptIdList = shiroService.getDataScopeList(userDetail.getId());
            userDetail.setDeptIdList(deptIdList);

            if (userDetail.getStatus() == 0) {
                throw new LockedAccountException(MessageUtils.getMessage(ErrorCode.ACCOUNT_LOCK));
            }
        } else {
            CliUserEntity userEntity = shiroService.getCliUser(cliTokenEntity.getUserId());
            userDetail = ConvertUtils.sourceToTarget(userEntity, UserDetail.class);
        }

        return new SimpleAuthenticationInfo(userDetail, accessToken, getName());
    }

}