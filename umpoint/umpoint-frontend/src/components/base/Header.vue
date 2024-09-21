<template>
    <div id="header">
        <div id="header-wrap" class="container">
            <div id="logo">
                <img
                    src="@/assets/umlogo.png"
                    alt="Universiti Malaya"
                    style="height: 100px"
                />
            </div>
            <div>
                <el-dropdown placement="bottom-end">
                    <el-button type="primary" plain class="account">
                        <svg-icon type="mdi" :path="mdiAccount" />
                        <svg-icon type="mdi" :path="mdiMenuDown" />
                    </el-button>
                    <template #dropdown>
                        <el-dropdown-menu>
                            <el-dropdown-item>
                                <router-link to="/profile">
                                    <svg-icon type="mdi" :path="mdiAccount" />
                                    Profile
                                </router-link>
                            </el-dropdown-item>
                            <el-dropdown-item>
                                <router-link to="/bookings">
                                    <svg-icon type="mdi" :path="mdiHistory" />
                                    Booking History
                                </router-link>
                            </el-dropdown-item>
                            <el-dropdown-item>
                                <router-link to="/chat">
                                    <svg-icon
                                        type="mdi"
                                        :path="mdiForumOutline"
                                    />
                                    Chats
                                </router-link>
                            </el-dropdown-item>
                            <el-dropdown-item divided>
                                <router-link to="/logout">
                                    <svg-icon type="mdi" :path="mdiLogout" />
                                    Logout
                                </router-link>
                            </el-dropdown-item>
                        </el-dropdown-menu>
                    </template>
                </el-dropdown>
            </div>
        </div>
    </div>
</template>

<script setup>
import {
    mdiAccount,
    mdiForumOutline,
    mdiHistory,
    mdiLogout,
    mdiMenuDown,
} from "@mdi/js";
</script>

<style>
#header {
    position: relative;
    background-color: #fff;
    border-bottom: 1px solid #f5f5f5;
}

#header-wrap {
    position: relative;
    background-color: #fff;
    width: 100%;

    display: flex;
    flex-direction: row;
    flex-wrap: wrap;
    align-items: center;
    justify-content: space-between;
}

#header #logo {
    position: relative;
    max-height: 100%;
}

.el-button.account {
    padding-inline: 6px 0;
}

.el-dropdown-menu__item {
    /* transfer padding to a such that hover box takes effect */
    padding: 0;
    color: var(--el-text-color-regular);

    &:hover {
        color: var(--el-color-primary);
    }

    > a {
        width: 100%;
        padding-block: 6px;
        padding-inline: 1em;
        text-decoration: none;
        color: inherit;

        > svg {
            vertical-align: text-bottom;
            height: 1.2em;
            width: 1.2em;
        }
    }
}
</style>
