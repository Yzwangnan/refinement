###说明
该服务为正元形象进度管理子系统与成本管理子系统整合的分布式微服务项目，采用统一登录服务中心与授权服务中心，
后期需要增加其它子系统可直接在父 module 下新建

###模块

####refinement-quality-manage-back
形象进度管理子系统服务 module

####refinement-cost-manage-back
成本管理子系统服务 module

####refinement-gateway
微服务网关中心：用于请求转发与用户角色权限校验

####refinement-oauth2
授权中心：加载角色对应的资源数据，获取用户登录的唯一标识 token  

####授权接口 /auth/oauth/token
| 请求参数 | 说明 | 固定值 |
|---|---|---|
| grant_type | 认证类型 | password |
| client_id | 客户端id | client-app |
| client_secret | 客户端秘钥 | 123456 |
| username | 用户名 | 无 |
| password | 密码 | 无 |

| 返回参数 | 说明 |
|---|---|
| token | 访问令牌 | 
| refreshToken | 刷新令牌 |
| tokenHead | 访问令牌头前缀 |
| expiresIn | 有效时间（秒） |

####refinement-login
统一登录中心

####refinement-common
公共 module 

####refinement-entity
实体类 module