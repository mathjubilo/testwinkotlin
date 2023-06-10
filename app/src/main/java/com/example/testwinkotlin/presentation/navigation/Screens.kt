package com.example.testwinkotlin.presentation.navigation

import com.example.testwinkotlin.R


sealed class Screens(var title:String, var icon:Int, var route:String){

    object ActiveIncidentsScreen : Screens(
        title = "Active \nIncidents",
        icon = R.drawable.critic_svg,
        route = "active_incidents"
    )

    object FollowedIncidentsScreen: Screens(
        "Followed \nIncidents",
        R.drawable.critic_svg,
        "followed_incidents"
    )
    object Filters: Screens("Filters",R.drawable.critic_svg,
        "filters")
    object DistributionCentersScreen: Screens("Distribution \nCenters",R.drawable.critic_svg,"distribution_centers")
    object MyProfileScreen: Screens("My Profile", R.drawable.critic_svg,"my_profile")
    object IncidentDetailsScreen: Screens("Incident Details", R.drawable.critic_svg,"incident_details")
    object MessagesAndWorklogsScreen: Screens("Messages And Worklogs", R.drawable.critic_svg,"messages_and_worklogs")
    object LaunchScreen: Screens("Launch", R.drawable.critic_svg,"launch")
    object Login: Screens("Login", R.drawable.critic_svg,"login")
    object MainNavGraphRoute: Screens("Main", R.drawable.critic_svg,"main_graph")
    object AuthNavGraphRoute: Screens("Auth", R.drawable.critic_svg,"auth_graph")
    object RootNavGraphRoute: Screens("Root", R.drawable.critic_svg,"root_graph")
}