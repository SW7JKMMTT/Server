package rocks.stalin.sw708e16.server.persistence.hibernate;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import rocks.stalin.sw708e16.server.core.authentication.Permission;
import rocks.stalin.sw708e16.server.persistence.PermissionDao;

@Transactional
@Repository
@Primary
public class PermissionDaoImpl extends BaseDaoImpl<Permission> implements PermissionDao {
}
