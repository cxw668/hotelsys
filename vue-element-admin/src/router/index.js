import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

/* Layout */
import Layout from '@/layout'

/**
 * Note: sub-menu only appear when route children.length >= 1
 * Detail see: https://panjiachen.github.io/vue-element-admin-site/guide/essentials/router-and-nav.html
 *
 * hidden: true                   if set true, item will not show in the sidebar(default is false)
 * alwaysShow: true               if set true, will always show the root menu
 *                                if not set alwaysShow, when item has more than one children route,
 *                                it will becomes nested mode, otherwise not show the root menu
 * redirect: noRedirect           if set noRedirect will no redirect in the breadcrumb
 * name:'router-name'             the name is used by <keep-alive> (must set!!!)
 * meta : {
 *    roles: ['admin','editor']    control the page roles (you can set multiple roles)
 *    title: 'title'               the name show in sidebar and breadcrumb (recommend set)
 *    icon: 'svg-name'/'el-icon-x' the icon show in the sidebar
 *    noCache: true                if set true, the page will no be cached(default is false)
 *    affix: true                  if set true, the tag will affix in the tags-view
 *    breadcrumb: false            if set false, the item will hidden in breadcrumb(default is true)
 *    activeMenu: '/example/list'  if set path, the sidebar will highlight the path you set
 *  }
 */

/**
 * constantRoutes
 * a base page that does not have permission requirements
 * all roles can be accessed
 */
export const constantRoutes = [
  {
    path: '/redirect',
    component: Layout,
    hidden: true,
    children: [
      {
        path: '/redirect/:path(.*)',
        component: () => import('@/views/redirect/index')
      }
    ]
  },
  {
    path: '/login',
    component: () => import('@/views/login/index'),
    hidden: true
  },
  {
    path: '/auth-redirect',
    component: () => import('@/views/login/auth-redirect'),
    hidden: true
  },
  {
    path: '/404',
    component: () => import('@/views/error-page/404'),
    hidden: true
  },
  {
    path: '/401',
    component: () => import('@/views/error-page/401'),
    hidden: true
  },
  {
    path: '/',
    component: Layout,
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        component: () => import('@/views/dashboard/index'),
        name: 'Dashboard',
        meta: { title: '首页仪表盘', icon: 'dashboard', affix: true }
      }
    ]
  }
]

/**
 * asyncRoutes
 * the routes that need to be dynamically loaded based on user roles
 */
export const asyncRoutes = [

  // 1. Rooms
  {
    path: '/rooms',
    component: Layout,
    children: [
      {
        path: 'index',
        component: () => import('@/views/rooms/index'),
        name: 'Rooms',
        meta: { title: '客房管理', icon: 'house' }
      }
    ]
  },

  // 2. Stats
  {
    path: '/stats',
    component: Layout,
    children: [
      {
        path: 'index',
        component: () => import('@/views/stats/index'),
        name: 'Stats',
        meta: { title: '经营数据', icon: 'chart' }
      }
    ]
  },

  // 3. Reservations
  {
    path: '/reservations',
    component: Layout,
    children: [
      {
        path: 'index',
        component: () => import('@/views/reservations/index'),
        name: 'Reservations',
        meta: { title: '客房预订', icon: 'documentation' }
      }
    ]
  },

  // 4. Customers
  {
    path: '/customers',
    component: Layout,
    children: [
      {
        path: 'index',
        component: () => import('@/views/customers/index'),
        name: 'Customers',
        meta: { title: '客户管理', icon: 'peoples' }
      }
    ]
  },

  // 5. Orders
  {
    path: '/orders',
    component: Layout,
    children: [
      {
        path: 'index',
        component: () => import('@/views/orders/index'),
        name: 'Orders',
        meta: { title: '订单管理', icon: 'shopping' }
      }
    ]
  },

  // 6. Employees
  {
    path: '/employees',
    component: Layout,
    children: [
      {
        path: 'index',
        component: () => import('@/views/employees/index'),
        name: 'Employees',
        meta: { title: '员工管理', icon: 'user' }
      }
    ]
  },

  // 7. Pricing Policies
  {
    path: '/pricing-policies',
    component: Layout,
    children: [
      {
        path: 'index',
        component: () => import('@/views/pricing-policies/index'),
        name: 'PricingPolicies',
        meta: { title: '价格策略', icon: 'money' }
      }
    ]
  },

  // 8. Checkouts
  {
    path: '/checkouts',
    component: Layout,
    children: [
      {
        path: 'index',
        component: () => import('@/views/checkouts/index'),
        name: 'Checkouts',
        meta: { title: '退房结算', icon: 'exit-fullscreen' } // approximated icon
      }
    ]
  },

  // 9. Room Cleaning Status
  {
    path: '/room-cleaning-statuses',
    component: Layout,
    children: [
      {
        path: 'index',
        component: () => import('@/views/room-cleaning-statuses/index'),
        name: 'RoomCleaningStatuses',
        meta: { title: '清洁状态', icon: 'drag' } // approximated icon
      }
    ]
  },

  // 10. Complaints
  {
    path: '/complaints',
    component: Layout,
    children: [
      {
        path: 'index',
        component: () => import('@/views/complaints/index'),
        name: 'Complaints',
        meta: { title: '投诉报修', icon: 'message' }
      }
    ]
  },

  // 11. Inventory
  {
    path: '/inventories',
    component: Layout,
    children: [
      {
        path: 'index',
        component: () => import('@/views/inventories/index'),
        name: 'Inventories',
        meta: { title: '库存管理', icon: 'list' }
      }
    ]
  },

  // 12. Memberships
  {
    path: '/memberships',
    component: Layout,
    children: [
      {
        path: 'index',
        component: () => import('@/views/memberships/index'),
        name: 'Memberships',
        meta: { title: '会员管理', icon: 'star' }
      }
    ]
  },

  // 404 page must be placed at the end !!!
  { path: '*', redirect: '/404', hidden: true }
]

const createRouter = () => new Router({
  // mode: 'history', // require service support
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRoutes
})

const router = createRouter()

// Detail see: https://github.com/vuejs/vue-router/issues/1234#issuecomment-357941465
export function resetRouter() {
  const newRouter = createRouter()
  router.matcher = newRouter.matcher // reset router
}

export default router
