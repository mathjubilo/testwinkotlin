package com.example.testwinkotlin.presentation.incidentsView

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class IncidentsViewModel
@Inject constructor(

): ViewModel() {

    init {

    }
    // EL PARAMETRO DE LA PAGINA, DE ORDER Y LIMIT::::::
    /*
    El view mdoel es quien se va a encargar de guardar en qué página del listado estamos. Lo
    ponemos aquí porque kuego el listado y las llamadas a la API de WIN van a tener que utilizar
    el valor de la página. Así que el view model como está en el medio, va a saber del valor para
    comunicarlo a las otras clases
    */



    // LOS FILTROS DE LAS VISTAS
    /*
    Los filtros de las vistas van a estar en una clase estática que va a contener todos los filtros
    de cada vista, de esta manera siempre que queramos buscar algún filtro de alguna vista, solo
    tenemos que acceder a la clase estática global de AppFilters
    Va a ser un companion object porque luego vamos a necesitar cambiar estos valores desde
    la vista de launch screen
     */


    // LOS FILTROS VAN A SER BUSCADOS DE DATASTORE Y LUEGO CARGADOS EN SUS COMPANION OBJECT
    /*
    De esta manera ya tenemos la funcionalidad de guardar los filtros y cuando el usuario entre
    en la app, van a aparecer los últimos filtros
     */

}