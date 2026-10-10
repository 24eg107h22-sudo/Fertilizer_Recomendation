/**
 * Main Application Logic for Fertilizer Recommendation System
 */

// State Management
const AppState = {
  currentUser: JSON.parse(localStorage.getItem('fertilizer_user')) || {
    id: 1,
    name: 'Ramesh Kumar',
    email: 'farmer@example.com',
    phone: '9876543210',
    location: 'Punjab, India'
  },
  currentRecommendation: null,
  history: [],
  crops: []
};

// DOM Content Loaded
document.addEventListener('DOMContentLoaded', async () => {
  initNavigation();
  initAuth();
  initFormControls();
  initPresets();
  await loadCrops();
  updateUserUI();
  await refreshHistory();
});

// Toast notification helper
function showToast(message, type = 'info') {
  const toast = document.getElementById('toast');
  if (!toast) return;
  toast.textContent = message;
  toast.className = `toast show ${type}`;
  setTimeout(() => {
    toast.className = 'toast';
  }, 3500);
}

// Navigation between views
function initNavigation() {
  const links = document.querySelectorAll('[data-view-target]');
  links.forEach(link => {
    link.addEventListener('click', (e) => {
      e.preventDefault();
      const targetView = link.getAttribute('data-view-target');
      switchView(targetView);
    });
  });
}

function switchView(viewId) {
  document.querySelectorAll('.view-section').forEach(sec => sec.classList.remove('active'));
  document.querySelectorAll('.nav-link').forEach(link => link.classList.remove('active'));

  const targetSection = document.getElementById(viewId);
  if (targetSection) {
    targetSection.classList.add('active');
  }

  const activeNavLink = document.querySelector(`.nav-link[data-view-target="${viewId}"]`);
  if (activeNavLink) {
    activeNavLink.classList.add('active');
  }

  window.scrollTo({ top: 0, behavior: 'smooth' });

  if (viewId === 'history-view') {
    refreshHistory();
  }
}

// Authentication handlers
function initAuth() {
  const loginModal = document.getElementById('loginModal');
  const registerModal = document.getElementById('registerModal');

  // Open modals
  document.getElementById('loginNavBtn')?.addEventListener('click', () => {
    loginModal.classList.add('active');
  });
  document.getElementById('registerNavBtn')?.addEventListener('click', () => {
    registerModal.classList.add('active');
  });

  // Close buttons
  document.querySelectorAll('.close-modal-btn').forEach(btn => {
    btn.addEventListener('click', () => {
      loginModal.classList.remove('active');
      registerModal.classList.remove('active');
    });
  });

  // Login form submit
  document.getElementById('loginForm')?.addEventListener('submit', async (e) => {
    e.preventDefault();
    const email = document.getElementById('loginEmail').value;
    const password = document.getElementById('loginPassword').value;

    const res = await ApiService.login({ email, password });
    if (res.success) {
      AppState.currentUser = {
        id: res.userId,
        name: res.name,
        email: res.email,
        phone: res.phone,
        location: res.location
      };
      localStorage.setItem('fertilizer_user', JSON.stringify(AppState.currentUser));
      updateUserUI();
      loginModal.classList.remove('active');
      showToast(`Welcome back, ${res.name}!`, 'success');
    } else {
      showToast(res.message || 'Login failed', 'error');
    }
  });

  // Register form submit
  document.getElementById('registerForm')?.addEventListener('submit', async (e) => {
    e.preventDefault();
    const name = document.getElementById('regName').value;
    const email = document.getElementById('regEmail').value;
    const password = document.getElementById('regPassword').value;
    const phone = document.getElementById('regPhone').value;
    const location = document.getElementById('regLocation').value;

    const res = await ApiService.register({ name, email, password, phone, location });
    if (res.success) {
      AppState.currentUser = {
        id: res.userId,
        name: res.name,
        email: res.email,
        phone: res.phone,
        location: res.location
      };
      localStorage.setItem('fertilizer_user', JSON.stringify(AppState.currentUser));
      updateUserUI();
      registerModal.classList.remove('active');
      showToast('Registration successful! Welcome!', 'success');
    } else {
      showToast(res.message || 'Registration failed', 'error');
    }
  });

  // Demo user fill button
  document.getElementById('loadDemoUserBtn')?.addEventListener('click', () => {
    document.getElementById('loginEmail').value = 'farmer@example.com';
    document.getElementById('loginPassword').value = 'password123';
  });

  // Logout button
  document.getElementById('logoutBtn')?.addEventListener('click', () => {
    AppState.currentUser = null;
    localStorage.removeItem('fertilizer_user');
    updateUserUI();
    showToast('Logged out successfully.', 'info');
    switchView('home-view');
  });
}

function updateUserUI() {
  const user = AppState.currentUser;
  const userContainer = document.getElementById('userNavContainer');
  const guestContainer = document.getElementById('guestNavContainer');
  const dashFarmerName = document.getElementById('dashFarmerName');
  const dashFarmerLoc = document.getElementById('dashFarmerLoc');

  if (user && user.name) {
    if (userContainer) userContainer.style.display = 'flex';
    if (guestContainer) guestContainer.style.display = 'none';
    const nameSpan = document.getElementById('loggedUserName');
    if (nameSpan) nameSpan.textContent = user.name;
    if (dashFarmerName) dashFarmerName.textContent = user.name;
    if (dashFarmerLoc) dashFarmerLoc.textContent = user.location || 'Active Region';
  } else {
    if (userContainer) userContainer.style.display = 'none';
    if (guestContainer) guestContainer.style.display = 'flex';
    if (dashFarmerName) dashFarmerName.textContent = 'Guest Farmer';
    if (dashFarmerLoc) dashFarmerLoc.textContent = 'Demo Mode';
  }
}

// Load Crops dropdown
async function loadCrops() {
  const cropSelect = document.getElementById('cropSelect');
  if (!cropSelect) return;

  AppState.crops = await ApiService.getCrops();
  cropSelect.innerHTML = '<option value="" disabled selected>-- Select Target Crop --</option>';
  AppState.crops.forEach(c => {
    const opt = document.createElement('option');
    opt.value = c.cropName;
    opt.textContent = `${c.cropName} (${c.category || 'Crop'})`;
    cropSelect.appendChild(opt);
  });
}

// Form Controls & Sliders
function initFormControls() {
  const phSlider = document.getElementById('phSlider');
  const phInput = document.getElementById('phInput');
  const phBadge = document.getElementById('phBadge');

  if (phSlider && phInput) {
    const updatePh = (val) => {
      phSlider.value = val;
      phInput.value = val;
      if (val < 6.0) {
        phBadge.textContent = 'Acidic';
        phBadge.className = 'badge badge-low';
      } else if (val <= 7.5) {
        phBadge.textContent = 'Optimal';
        phBadge.className = 'badge badge-optimal';
      } else {
        phBadge.textContent = 'Alkaline';
        phBadge.className = 'badge badge-high';
      }
    };

    phSlider.addEventListener('input', (e) => updatePh(e.target.value));
    phInput.addEventListener('input', (e) => updatePh(e.target.value));
  }

  // Handle recommendation form submission
  const form = document.getElementById('recommendationForm');
  if (form) {
    form.addEventListener('submit', async (e) => {
      e.preventDefault();
      const submitBtn = document.getElementById('submitBtn');
      const originalText = submitBtn.innerHTML;
      submitBtn.innerHTML = 'Analyzing Soil & Crop Needs...';
      submitBtn.disabled = true;

      const payload = {
        userId: AppState.currentUser ? AppState.currentUser.id : null,
        cropName: document.getElementById('cropSelect').value,
        soilType: document.getElementById('soilType').value,
        nitrogen: parseFloat(document.getElementById('inputN').value),
        phosphorus: parseFloat(document.getElementById('inputP').value),
        potassium: parseFloat(document.getElementById('inputK').value),
        ph: parseFloat(document.getElementById('phInput').value),
        moisture: parseFloat(document.getElementById('inputMoisture').value || 55),
        temperature: parseFloat(document.getElementById('inputTemp').value || 28),
        humidity: parseFloat(document.getElementById('inputHumidity').value || 65),
        season: document.getElementById('inputSeason').value
      };

      try {
        const result = await ApiService.generateRecommendation(payload);
        AppState.currentRecommendation = result;
        renderRecommendationResult(result);
        switchView('result-view');
        showToast('Fertilizer Recommendation Generated Successfully!', 'success');
        refreshHistory();
      } catch (err) {
        showToast('Error generating recommendation: ' + err.message, 'error');
      } finally {
        submitBtn.innerHTML = originalText;
        submitBtn.disabled = false;
      }
    });
  }
}

// Preset Buttons for rapid testing
function initPresets() {
  document.getElementById('presetRiceLowN')?.addEventListener('click', () => {
    setFormValues('Rice', 'Clayey', 40, 45, 45, 6.5, 75, 29, 70, 'Kharif');
  });

  document.getElementById('presetWheatLowP')?.addEventListener('click', () => {
    setFormValues('Wheat', 'Alluvial / Loamy', 110, 20, 45, 6.8, 60, 22, 55, 'Rabi');
  });

  document.getElementById('presetPotatoLowK')?.addEventListener('click', () => {
    setFormValues('Potato', 'Sandy Loam', 120, 90, 35, 5.8, 65, 20, 60, 'Rabi');
  });

  document.getElementById('presetBalanced')?.addEventListener('click', () => {
    setFormValues('Maize', 'Black Soil', 115, 58, 48, 7.0, 60, 28, 65, 'Kharif');
  });
}

function setFormValues(crop, soil, n, p, k, ph, moisture, temp, humidity, season) {
  document.getElementById('cropSelect').value = crop;
  document.getElementById('soilType').value = soil;
  document.getElementById('inputN').value = n;
  document.getElementById('inputP').value = p;
  document.getElementById('inputK').value = k;
  document.getElementById('phSlider').value = ph;
  document.getElementById('phInput').value = ph;
  document.getElementById('inputMoisture').value = moisture;
  document.getElementById('inputTemp').value = temp;
  document.getElementById('inputHumidity').value = humidity;
  document.getElementById('inputSeason').value = season;
  document.getElementById('phInput').dispatchEvent(new Event('input'));
  showToast(`Loaded preset for ${crop}`, 'info');
}

// Render Recommendation Result
function renderRecommendationResult(rec) {
  document.getElementById('resFertilizerName').textContent = rec.fertilizerName;
  document.getElementById('resCropName').textContent = rec.cropName;
  document.getElementById('resSoilType').textContent = rec.soilType || 'Soil';
  document.getElementById('resDosage').textContent = rec.quantity;
  document.getElementById('resMethod').textContent = rec.applicationMethod;
  document.getElementById('resInstructions').textContent = rec.usageInstructions;
  document.getElementById('resOrganicAdvice').textContent = rec.organicAdvice || 'Maintain optimal organic matter with decomposed compost.';
  
  const scoreElem = document.getElementById('resHealthScore');
  if (scoreElem) scoreElem.textContent = `${rec.soilHealthScore || 85} / 100`;

  // Status badges
  document.getElementById('resStatusN').textContent = rec.nitrogenStatus;
  document.getElementById('resStatusP').textContent = rec.phosphorusStatus;
  document.getElementById('resStatusK').textContent = rec.potassiumStatus;
  document.getElementById('resStatusPh').textContent = rec.phStatus;

  // Render Nutrient Breakdown Bars
  const breakdownContainer = document.getElementById('resNutrientBars');
  if (breakdownContainer && rec.nutrientBreakdown) {
    breakdownContainer.innerHTML = '';
    rec.nutrientBreakdown.forEach(item => {
      const percentage = Math.min(100, Math.round((item.soilLevel / Math.max(1, item.targetLevel)) * 100));
      const fillClass = item.status.includes('Low') ? 'low' : item.status.includes('High') ? 'high' : 'optimal';
      
      const div = document.createElement('div');
      div.className = 'nutrient-bar-container';
      div.innerHTML = `
        <div class="nutrient-bar-header">
          <span><strong>${item.nutrient}</strong>: ${item.soilLevel} kg/ha (Target: ${item.targetLevel} kg/ha)</span>
          <span class="badge badge-${fillClass}">${item.status}</span>
        </div>
        <div class="progress-track">
          <div class="progress-fill ${fillClass}" style="width: ${percentage}%"></div>
        </div>
        <div class="form-hint">${item.role}</div>
      `;
      breakdownContainer.appendChild(div);
    });
  }
}

// Refresh and Render History
async function refreshHistory() {
  const userId = AppState.currentUser ? AppState.currentUser.id : null;
  AppState.history = await ApiService.getHistory(userId);
  renderHistoryTable(AppState.history);

  // Update total stats
  const totalCountElem = document.getElementById('dashTotalRecs');
  if (totalCountElem) totalCountElem.textContent = AppState.history.length;
}

function renderHistoryTable(items) {
  const tbody = document.getElementById('historyTableBody');
  const emptyState = document.getElementById('historyEmptyState');
  if (!tbody) return;

  if (!items || items.length === 0) {
    tbody.innerHTML = '';
    if (emptyState) emptyState.style.display = 'block';
    return;
  }

  if (emptyState) emptyState.style.display = 'none';
  tbody.innerHTML = '';

  items.forEach(item => {
    const tr = document.createElement('tr');
    const dateStr = item.createdDate ? new Date(item.createdDate).toLocaleDateString() : 'Today';
    tr.innerHTML = `
      <td><strong>${item.cropName}</strong></td>
      <td>${item.soilType || 'N/A'}</td>
      <td><span style="font-weight: 600; color: #047857;">${item.fertilizerName}</span></td>
      <td>${item.quantity}</td>
      <td>${dateStr}</td>
      <td>
        <button class="btn btn-secondary btn-sm" onclick="viewHistoryDetails(${item.id})">View</button>
        <button class="btn btn-danger btn-sm" onclick="deleteHistoryItem(${item.id})">Delete</button>
      </td>
    `;
    tbody.appendChild(tr);
  });
}

// History actions
window.viewHistoryDetails = (id) => {
  const item = AppState.history.find(h => h.id === id);
  if (item) {
    AppState.currentRecommendation = item;
    renderRecommendationResult(item);
    switchView('result-view');
  }
};

window.deleteHistoryItem = async (id) => {
  if (confirm('Are you sure you want to delete this recommendation record?')) {
    await ApiService.deleteHistoryItem(id);
    showToast('Record deleted.', 'info');
    await refreshHistory();
  }
};

// Search filter in history
document.getElementById('historySearchInput')?.addEventListener('input', (e) => {
  const query = e.target.value.toLowerCase();
  const filtered = AppState.history.filter(h => 
    h.cropName.toLowerCase().includes(query) ||
    h.fertilizerName.toLowerCase().includes(query) ||
    (h.soilType && h.soilType.toLowerCase().includes(query))
  );
  renderHistoryTable(filtered);
});
