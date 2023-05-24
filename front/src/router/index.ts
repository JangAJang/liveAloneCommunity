import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'main',
      component: () => import('../components/post/AllPost.vue')
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
    },
    {
      path: '/auth/kakao-login',
      name: 'kakaoAuth',
      component: () => import('../components/auth/KakaoLogIn.vue'),
      meta: { unauthorized: true }
    },
    {
      path: '/member',
      name: 'myPage',
      component: ()=> import('../components/member/MyPage.vue'),
    },
    {
      path: '/post/write',
      name: 'writePost',
      component: () => import('../components/post/WritePost.vue')
    },
    {
      path: '/post/read/:postId',
      name: 'readPost',
      component: () => import('../components/post/PostView.vue'),
      props: true
    },
    {
      path: '/post/edit/:postId',
      name: 'editPost',
      component: () => import('../components/post/EditPostView.vue'),
      props: true
    },
    {
      path: '/comment/of/:postId',
      name: 'commentOfPost',
      component: () => import('../components/comment/CommentOfPost.vue'),
      props: true
    },
    {
      path: '/message/write',
      name: 'writeMessage',
      component: ()=> import('../components/message/WriteMessage.vue')
    }
  ]
})

export default router
