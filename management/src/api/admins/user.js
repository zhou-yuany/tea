import request from '@/utils/request'
export default{


    //管理员列表(条件查询分页)
    getAdminsListPage(page,limit,adminsQuery){
        return request({
            // url: '/eduservice/teacher/pageTeacherCondition/'+current+'/'+limit,
            url: `/users/getAdminsList/${page}/${limit}`,  // 飘号
            method: 'post',
            data: adminsQuery
          })
    },

    // 添加管理员信息
    addAdminsInfo(admins){
        return request({
            // url: '/eduservice/course/pageTeacherCondition/'+current+'/'+limit,
            url: `/users/addAdmins`,  // 飘号
            method: 'post',
            data:admins
        })
    },

    // 修改管理员信息
    updateAdmins(admins){
        return request({
            url: `/users/updateAdmins`,
            method: 'post',
            data: admins
        })
    },

    // 根据id查询管理员信息
    getAdminsInfo(id){
        return request({
            url: `/users/getAdmins/${id}`,
            method: 'get'
        })
    },

    // 删除管理员
    deleteAdmins(id){
        return request({
            url: `/users/remove/${id}`,
            method: 'delete'
        })
    },

    //用户授权列表(条件查询分页)
    getEmpowerListPage(page,limit,authUsersQuery){
        return request({
            // url: '/eduservice/teacher/pageTeacherCondition/'+current+'/'+limit,
            url: `/users/getEmpowerList/${page}/${limit}`,  // 飘号
            method: 'post',
            data: authUsersQuery
          })
    },

    // 授权天数
    updateAdminsDays(id, days){
        return request({
            // url: '/eduservice/course/pageTeacherCondition/'+current+'/'+limit,
            url: `/users/updateAdminsDays`,  // 飘号
            method: 'post',
            params: {id,days}
            
        })
    },
    

    //用户列表(条件查询分页)
    getUsersListPage(page,limit,usersQuery){
        return request({
            // url: '/eduservice/teacher/pageTeacherCondition/'+current+'/'+limit,
            url: `/users/getUsersList/${page}/${limit}`,  // 飘号
            method: 'post',
            data: usersQuery
          })
    },

    // 添加用户信息
    addUsersInfo(users){
        return request({
            // url: '/eduservice/course/pageTeacherCondition/'+current+'/'+limit,
            url: `/users/addUsers`,  // 飘号
            method: 'post',
            data:users
        })
    },

    // 修改用户信息
    updateUsers(users){
        return request({
            url: `/users/updateUsers`,
            method: 'post',
            data: users
        })
    },

    // 根据id查询用户信息
    getUsersInfo(id){
        return request({
            url: `/users/getUsers/${id}`,
            method: 'get'
        })
    },

    // 删除用户
    deleteUsers(id){
        return request({
            url: `/users/${id}`,
            method: 'delete'
        })
    }


}