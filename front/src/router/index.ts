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
      path: '/auth/logOut',
      name: 'logOut',
      component: () => import('../components/auth/LogOut.vue')
    },
    {
      path: '/member',
      name: 'myPage',
      component: () => import('../components/member/MyPage.vue')
    },
    {
      path: '/member/edit/nickname',
      name: 'editNickname',
      component: () => import('../components/member/EditNickname.vue')
    },
    {
      path: '/member/edit/location',
      name: 'editLocation',
      component: () => import('../components/member/ChangeLocation.vue')
    },
    {
      path: '/member/edit/password',
      name: 'editPassword',
      component: () => import('../components/member/EditPassword.vue')
    },
    {
      path: '/member/delete',
      name: 'deleteMember',
      component: () => import('../components/member/DeleteMember.vue')
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
      path: '/post/read/mine',
      name: 'myPost',
      component: () => import('../components/member/MyPost.vue')
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
      path: '/message',
      name: 'messageMain',
      component: () => import('../components/message/MessageMain.vue')
    },
    {
      path: '/message/write',
      name: 'writeMessage',
      component: () => import('../components/message/WriteMessage.vue')
    },
    {
      path: '/message/:messageId',
      name: 'messageView',
      component: () => import('../components/message/MessageView.vue'),
      props: true
    }
  ]
})

export default router
