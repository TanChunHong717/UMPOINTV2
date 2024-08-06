export enum EPageLayoutEnum {
  "page",
  "fullscreen"
}

export enum ESidebarLayoutEnum {
  Left = "left",
  Top = "top",
  Mix = "mix"
}

export enum EThemeSetting {
  Sidebar = "sidebar",
  TopHeader = "topHeader",
  ThemeColor = "themeColor",
  NavLayout = "navLayout",
  ContentFull = "contentFull",
  LogoAuto = "logoAuto",
  ColorIcon = "colorIcon",
  SidebarUniOpened = "sidebarUniOpened",
  OpenTabsPage = "openTabsPage",
  TabStyle = "tabStyle",
  SidebarCollapse = "sidebarCollapse"
}

export enum EMitt {
  OnLoading = "onLoading",
  OnSwitchLeftSidebar = "onSwitchLeftSidebar",
  OnPushMenuToTabs = "onPushMenuToTabs",
  OnSetTheme = "onSetTheme",
  OnSetThemeNotUniqueOpened = "onSetTheme_not_uniqueOpened",
  OnSetThemeTabsPage = "onSetTheme_tabsPage",
  OnSetNavLayout = "onSetNavLayout",
  OnReloadTabPage = "onReloadTabPage",
  OnMobileOpenSidebar = "onMobileOpenSidebar",
  OnSelectHeaderNavMenusByMixNav = "onSelectHeaderNavMenusByMixNav",
  OnCloseCurrTab = "onCloseCurrTab"
}

export enum EThemeColor {
  ThemeColor = "--color-primary"
}

export enum ButtonType {
  PRIMARY = 'primary',
  SUCCESS = 'success',
  WARNING = 'warning',
  DANGER = 'danger',
  INFO = 'info',
  TEXT = 'text'
}
