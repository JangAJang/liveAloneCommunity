import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'main',
      component: ()=> import('../components/post/AllPost.vue')
    },
    {
      path: '/auth/register',
      name: 'register',
      component: () => import('../components/auth/Register.vue'),
      meta: { unauthorized: true }
    },
    {
      path: '/auth/logIn',
      name: 'logIn',
      component: () => import('../components/auth/LogIn.vue'),
      meta: { unauthorized: true }
    }
  ]
})

export default router
