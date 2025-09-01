<template>
  <div class="auth-bg min-h-screen w-full">
    <div class="dz-topbar">
      <div class="inner">
        <img class="dz-logo" src="@/assets/dz_logo.png" alt="logo" />
        <img class="dz-logo2" src="@/assets/dz_logo2.png" alt="logo2" />
      </div>
    </div>
    <div class="flex items-center justify-center">
      <div class="auth-panel bg-white rounded-xl overflow-hidden">
        <slot />
      </div>
    </div>
  </div>
</template>

<style scoped>
.dz-topbar{ height: 70px; display:flex; align-items:center; }
.dz-topbar .inner{ width: 900px; margin: 0 auto; display:flex; align-items:center; justify-content:space-between; }
.dz-logo{ height: 46px; }
.dz-logo2{ height: 28px; }
</style>


