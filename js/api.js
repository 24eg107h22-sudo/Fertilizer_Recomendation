/**
 * API Service for Fertilizer Recommendation System
 * Connects Frontend with Spring Boot REST Endpoints
 */

const API_BASE_URL = window.location.hostname === 'localhost' || window.location.hostname === '127.0.0.1'
  ? 'http://localhost:8080/api'
  : '/api';

const ApiService = {
  // 1. Auth & Farmer Endpoints
  async register(farmerData) {
    try {
      const response = await fetch(`${API_BASE_URL}/auth/register`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(farmerData)
      });
      return await response.json();
    } catch (error) {
      console.warn('Backend unavailable, using simulated response:', error);
      return {
        success: true,
        message: 'Farmer registered locally (offline mode)',
        userId: Date.now(),
        name: farmerData.name,
        email: farmerData.email,
        phone: farmerData.phone,
        location: farmerData.location
      };
    }
  },

  async login(credentials) {
    try {
      const response = await fetch(`${API_BASE_URL}/auth/login`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(credentials)
      });
      return await response.json();
    } catch (error) {
      console.warn('Backend unavailable, fallback login mode:', error);
      return {
        success: true,
        message: 'Logged in as Demo Farmer',
        userId: 1,
        name: 'Ramesh Kumar',
        email: credentials.email || 'farmer@example.com',
        phone: '9876543210',
        location: 'Punjab, India'
      };
    }
  },

  async getFarmerProfile(userId) {
    try {
      const response = await fetch(`${API_BASE_URL}/auth/farmer/${userId}`);
      if (!response.ok) throw new Error('Profile fetch failed');
      return await response.json();
    } catch (error) {
      console.warn('Falling back to local farmer data:', error);
      return null;
    }
  },

  // 2. Crop Endpoints
  async getCrops() {
    try {
      const response = await fetch(`${API_BASE_URL}/crops`);
      if (!response.ok) throw new Error('Crop list fetch failed');
      return await response.json();
    } catch (error) {
      console.warn('Using default crop dataset:', error);
      return [
        { cropName: 'Rice', category: 'Cereal', targetN: 80, targetP: 40, targetK: 40, minPh: 5.5, maxPh: 7.0 },
        { cropName: 'Wheat', category: 'Cereal', targetN: 120, targetP: 60, targetK: 40, minPh: 6.0, maxPh: 7.5 },
        { cropName: 'Maize', category: 'Cereal', targetN: 120, targetP: 60, targetK: 50, minPh: 5.8, maxPh: 7.2 },
        { cropName: 'Cotton', category: 'Cash Crop', targetN: 100, targetP: 50, targetK: 50, minPh: 6.0, maxPh: 7.5 },
        { cropName: 'Sugarcane', category: 'Cash Crop', targetN: 150, targetP: 60, targetK: 90, minPh: 6.0, maxPh: 7.5 },
        { cropName: 'Potato', category: 'Vegetable', targetN: 120, targetP: 100, targetK: 120, minPh: 5.2, maxPh: 6.5 },
        { cropName: 'Tomato', category: 'Vegetable', targetN: 100, targetP: 60, targetK: 80, minPh: 6.0, maxPh: 7.0 },
        { cropName: 'Soybean', category: 'Legume', targetN: 30, targetP: 60, targetK: 40, minPh: 6.0, maxPh: 7.0 },
        { cropName: 'Groundnut', category: 'Oilseed', targetN: 25, targetP: 50, targetK: 40, minPh: 5.8, maxPh: 6.5 },
        { cropName: 'Chickpea (Gram)', category: 'Pulse', targetN: 20, targetP: 40, targetK: 20, minPh: 6.0, maxPh: 7.5 }
      ];
    }
  },

  // 3. Recommendation Endpoints
  async generateRecommendation(data) {
    try {
      const response = await fetch(`${API_BASE_URL}/recommendations/generate`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(data)
      });
      if (!response.ok) {
        const err = await response.json();
        throw new Error(err.message || 'Recommendation failed');
      }
      return await response.json();
    } catch (error) {
      console.warn('API error, computing recommendation locally:', error);
      return this.computeLocalRecommendation(data);
    }
  },

  async getHistory(userId) {
    try {
      const url = userId ? `${API_BASE_URL}/recommendations/history?userId=${userId}` : `${API_BASE_URL}/recommendations/history`;
      const response = await fetch(url);
      if (!response.ok) throw new Error('History fetch failed');
      return await response.json();
    } catch (error) {
      console.warn('Loading history from local storage:', error);
      const localHistory = localStorage.getItem('fertilizer_history');
      return localHistory ? JSON.parse(localHistory) : [];
    }
  },

  async deleteHistoryItem(id) {
    try {
      const response = await fetch(`${API_BASE_URL}/recommendations/${id}`, {
        method: 'DELETE'
      });
      return response.ok;
    } catch (error) {
      console.warn('Deleting from local history:', error);
      const list = JSON.parse(localStorage.getItem('fertilizer_history') || '[]');
      const filtered = list.filter(item => item.id !== id);
      localStorage.setItem('fertilizer_history', JSON.stringify(filtered));
      return true;
    }
  },

  // Local fallback calculation engine if backend is offline
  computeLocalRecommendation(req) {
    const n = Number(req.nitrogen);
    const p = Number(req.phosphorus);
    const k = Number(req.potassium);
    const ph = Number(req.ph);

    let fertilizerName = "NPK Complex (10:26:26) + Urea";
    let quantity = "60 kg/acre NPK + 25 kg/acre Urea";
    let method = "Basal application at sowing + top dress";
    let instructions = "Apply full basal dose at sowing. Top-dress with Urea at 30 days after sowing.";
    let nStatus = n < 50 ? "Low / Deficient" : n > 120 ? "High / Excess" : "Optimal / Balanced";
    let pStatus = p < 30 ? "Low / Deficient" : p > 70 ? "High / Excess" : "Optimal / Balanced";
    let kStatus = k < 30 ? "Low / Deficient" : k > 80 ? "High / Excess" : "Optimal / Balanced";

    if (n < 50 && p >= 30 && k >= 30) {
      fertilizerName = "Neem-Coated Urea (46% N)";
      quantity = "50 kg/acre (125 kg/ha)";
      method = "2 Split top-dressings";
      instructions = "Apply 30 kg at early vegetative stage, remaining 20 kg at panicle initiation.";
    } else if (p < 30 && n >= 50 && k >= 30) {
      fertilizerName = "Single Super Phosphate (SSP) or DAP";
      quantity = "70 kg/acre SSP";
      method = "Deep basal placement near root zone";
      instructions = "Incorporate 5 cm below seed level during sowing.";
    } else if (k < 30) {
      fertilizerName = "MOP (Muriate of Potash 60% K₂O)";
      quantity = "35 kg/acre";
      method = "Basal application during land preparation";
      instructions = "Incorporate into soil to enhance stem strength and drought resistance.";
    }

    let organicAdvice = ph < 6.0 
      ? "Acidic soil detected: Incorporate 250 kg/acre Agricultural Lime 2 weeks prior to sowing."
      : ph > 7.5
      ? "Alkaline soil detected: Apply Agricultural Gypsum at 200 kg/acre and farm yard manure."
      : "Soil pH is balanced. Apply 3-4 tons/acre farmyard manure for microbial health.";

    const rec = {
      id: Date.now(),
      cropName: req.cropName,
      soilType: req.soilType,
      fertilizerName: fertilizerName,
      quantity: quantity,
      applicationMethod: method,
      usageInstructions: instructions,
      organicAdvice: organicAdvice,
      nitrogenStatus: nStatus,
      phosphorusStatus: pStatus,
      potassiumStatus: kStatus,
      phStatus: ph < 6.0 ? "Acidic" : ph > 7.5 ? "Alkaline" : "Optimal / Neutral",
      soilHealthScore: 78,
      createdDate: new Date().toISOString(),
      nutrientBreakdown: [
        { nutrient: 'Nitrogen (N)', soilLevel: n, targetLevel: 80, deficit: Math.max(0, 80 - n), status: nStatus, role: 'Leaf and vegetative growth' },
        { nutrient: 'Phosphorus (P)', soilLevel: p, targetLevel: 45, deficit: Math.max(0, 45 - p), status: pStatus, role: 'Root development and seed vigor' },
        { nutrient: 'Potassium (K)', soilLevel: k, targetLevel: 45, deficit: Math.max(0, 45 - k), status: kStatus, role: 'Disease resistance and grain filling' }
      ]
    };

    // Store in local history cache
    const existing = JSON.parse(localStorage.getItem('fertilizer_history') || '[]');
    existing.unshift(rec);
    localStorage.setItem('fertilizer_history', JSON.stringify(existing));

    return rec;
  }
};
