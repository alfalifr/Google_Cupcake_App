/*
 * Copyright (C) 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.cupcake

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.cupcake.databinding.FragmentSummaryBinding

/**
 * [SummaryFragment] contains a summary of the order details with a button to share the order
 * via another app.
 */
class SummaryFragment : Fragment() {

    // Binding object instance corresponding to the fragment_summary.xml layout
    // This property is non-null between the onCreateView() and onDestroyView() lifecycle callbacks,
    // when the view hierarchy is attached to the fragment.
    private var binding: FragmentSummaryBinding? = null
    private val viewModel: OrderViewModel by activityViewModels()
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val fragmentBinding = FragmentSummaryBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = findNavController()

        binding?.apply {
            lifecycleOwner = viewLifecycleOwner
            xmlViewModel = viewModel
            xmlFragment = this@SummaryFragment
            sendButton.setOnClickListener { sendOrder() }
        }
    }

    /**
     * Submit the order by sharing out the order details to another app via an implicit intent.
     */
    fun sendOrder() {
        //Toast.makeText(activity, "Send Order", Toast.LENGTH_SHORT).show()
        val cupcakeStr = resources.getQuantityString(
            R.plurals.cupcake_count,
            viewModel.quantity.value!!,
            viewModel.quantity.value,
        )
        val msg = getString(
            R.string.order_details,
            cupcakeStr,
            viewModel.flavor.value,
            viewModel.pickupDate.value,
            viewModel.priceStr.value
        )

        val intent = Intent(Intent.ACTION_SEND)
            .setType("text/plain")
            .putExtra(Intent.EXTRA_SUBJECT, getString(R.string.new_cupcake_order))
            .putExtra(Intent.EXTRA_TEXT, msg)

        // Check if there is any other app provided for `Intent.ACTION_SEND`
        if(activity?.packageManager?.resolveActivity(intent, 0) != null) {
            startActivity(intent)
        } else {
            Toast.makeText(
                activity,
                "No other app available for sending the order",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    fun cancelOrder() {
        navController.navigate(R.id.action_summaryFragment_to_startFragment)
        viewModel.init()
    }

    /**
     * This fragment lifecycle method is called when the view hierarchy associated with the fragment
     * is being removed. As a result, clear out the binding object.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}