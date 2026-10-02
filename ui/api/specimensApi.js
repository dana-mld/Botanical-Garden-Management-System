import axios from 'axios'

const BASE = '/api/specimens'

const authHeader = () => {
  const token = localStorage.getItem('token')
  return token ? { Authorization: `Bearer ${token}` } : {}
}

export const specimensApi = {
  getByPlant: (plantId) => axios.get(`${BASE}/plant/${plantId}`),
  getById: (id) => axios.get(`${BASE}/${id}`),
  create: (data) => axios.post(BASE, data, { headers: authHeader() }),
  update: (id, data) => axios.put(`${BASE}/${id}`, data, { headers: authHeader() }),
  delete: (id) => axios.delete(`${BASE}/${id}`, { headers: authHeader() }),
}