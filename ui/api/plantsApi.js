import axios from 'axios'

const BASE = '/api/plants'

const authHeader = () => {
  const token = localStorage.getItem('token')
  return token ? { Authorization: `Bearer ${token}` } : {}
}

export const plantsApi = {
  getAll: (params) => axios.get(BASE, { params }),
  getById: (id) => axios.get(`${BASE}/${id}`),
  search: (query) => axios.get(`${BASE}/search`, { params: { q: query } }),
  create: (data) => axios.post(BASE, data, { headers: authHeader() }),
  update: (id, data) => axios.put(`${BASE}/${id}`, data, { headers: authHeader() }),
  delete: (id) => axios.delete(`${BASE}/${id}`, { headers: authHeader() }),
}