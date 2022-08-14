import axios from "axios"

export default {
  state: {
    accessToken: localStorage.getItem('accessToken') || '',
    refreshToken: localStorage.getItem('refreshToken') || '',
    userid: localStorage.getItem('userid') || '',
    // accessToken: '',
    // refreshToken: '',
    // userid: '',
    currentUser: {},
    profile: {},
    authError: null,
  },
  getters: {
    isLoggedIn: state => !!state.refreshToken,
    currentUser: state => state.currentUser,
    profile: state => state.profile,
    authError: state => state.authError
  },
  mutations: {
    SET_Access_TOKEN: (state, accessToken) => state.accessToken = accessToken,
    SET_Refresh_TOKEN: (state, refreshToken) => state.refreshToken = refreshToken,
    SET_USERID: (state, userid) => state.userid = userid,
    SET_CURRENT_USER: (state, user) => state.currentUser = user,
    SET_PROFILE: (state, profile) => state.profile = profile,
    SET_AUTH_ERROR: (state, error) => state.authError = error,
  },
  actions: {
    saveAccessToken({ commit }, accessToken) {
      commit('SET_Access_TOKEN', accessToken)
      localStorage.setItem('accessToken', accessToken)
      // console.log(localStorage)
    },
    getters: {
        isLoggedIn: state => !!state.token,
        currentUser: state => state.currentUser,
        profile: state => state.profile,
        authError: state => state.authError,
        // authHeader: state => ({ Authorization: `Token ${state.token}`}),
    },
    saveUserid({ commit }, userid) {
      commit('SET_USERID', userid)
      localStorage.setItem('userid', userid)
    },
    removeToken({ commit }) {
      commit('SET_Access_TOKEN', '')
      commit('SET_Refresh_TOKEN', '')
      localStorage.setItem('accessToken', '')
      localStorage.setItem('refreshToken', '')
    },
    removeUserid({ commit }) {
      commit('SET_USERID', '')
      localStorage.setItem('userid', '')
    },
    login({ commit, dispatch }, credentials) {
      console.log("로그인")
      console.log(credentials)
      // if (credentials.saveId) {
      //   this.$cookies.set("idCookie", credentials.id);
      // }
      axios({
        url: 'https://i7a606.q.ssafy.io/auth-api/auth/login',
        method: 'post',
        data: credentials
      })
        .then(res => {
          console.log(res.data)
          const accessToken = res.data.accessToken
          const refreshToken = res.data.refreshToken
          const userid = res.data.userid
          dispatch('saveAccessToken', accessToken)
          dispatch('saveRefreshToken', refreshToken)
          dispatch('saveUserid', userid)
          dispatch('fetchCurrentUser')
          router.push({ name: 'components' })
        })
        .catch(err => {
          console.log(err.response)
          commit('SET_AUTH_ERROR', err.response.data)
        })
    },
    logout({ dispatch }) {
      // console.log(this.state.accounts.accessToken)
      if (!this.state.accounts.accessToken) {
        router.push({ name: 'login' })
      }
      axios({
        url: 'https://i7a606.q.ssafy.io/auth-api/auth/logout',
        method: 'post',
        data: {
          "accessToken": this.state.accounts.accessToken,
          "refreshToken": this.state.accounts.refreshToken
        }
      })
        .then(res => {
          dispatch('removeToken')
          dispatch('removeUserid')
          alert("로그아웃 되었습니다.")
          router.push({ name: 'login' })
        })
        .catch(err => {
          console.log("로그아웃 실패!")
          console.log(err.response)
        })
    },
    actions: {
        saveToken({ commit }, token) {
            commit('SET_TOKEN', token)
            localStorage.setItem('token', token)
        },
        removeToken({ commit }) {
            commit('SET_TOKEN', '')
            localStorage.setItem('token', '')
        },

        login({ commit, dispatch }, credentials) {
            axios({
                url: `${state.host}/auth-api/auth/login`,
                method: 'post',
                data: credentials
            })
            .then(res => {
                const token = res.data.key
                console.log(token)
                dispatch('saveToken', token)
                dispatch('fetchCurrentUser')
                router.push({ name: 'components' })
            })
            .catch(err => {
                console.log(err.response.data)
                commit('SET_AUTH_ERROR', err.response.data)
            })
        },

        fetchCurrentUser({ commit, getters, dispatch }) {
            if (getters.isLoggedIn) {
              axios({
                url: `${state.host}/auth-api/user/${state.userid}`,
                method: 'get',
              })
                .then(res => commit('SET_CURRENT_USER', res.data, console.log(res)))
                .catch(err => {
                  if (err.response.status === 401) {
                    dispatch('removeToken')
                    router.push({ name: 'login' })
                  }
                })
            }
        },
    },
}
  
  export default userStore