import axios from 'axios'

const authHeader = () => {
  const token = localStorage.getItem('token')
  return token ? { Authorization: `Bearer ${token}` } : {}
}

export const authApi = {
  login: (credentials) => axios.post('/api/auth/login', credentials),
}

export const usersApi = {
  getAll: (params) => axios.get('/api/users', { headers: authHeader(), params }),
  getById: (id) => axios.get(`/api/users/${id}`, { headers: authHeader() }),
  create: (data) => axios.post('/api/users', data, { headers: authHeader() }),
  update: (id, data) => axios.put(`/api/users/${id}`, data, { headers: authHeader() }),
  delete: (id) => axios.delete(`/api/users/${id}`, { headers: authHeader() }),
  notify: (id) => axios.post(`/api/users/${id}/notify`, {}, { headers: authHeader() }),
}