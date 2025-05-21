package ru.itis.androidhomework.auth

import dagger.hilt.android.AndroidEntryPoint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import ru.itis.androidhomework.auth.databinding.FragmentLoginBinding

@AndroidEntryPoint
class AuthFragment : Fragment(R.layout.fragment_login) {

    private var viewBinding: FragmentLoginBinding? = null

    private val viewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentLoginBinding.inflate(inflater, container, false)
        return viewBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.loginState.collect { state ->
                when (state) {
                    is LoginState.Loading -> {
                        viewBinding?.progressBar?.visibility = View.VISIBLE
                        viewBinding?.btnLogin?.isEnabled = false
                    }

                    is LoginState.Success -> {
                        viewBinding?.progressBar?.visibility = View.GONE
                        viewBinding?.btnLogin?.isEnabled = true
                        viewModel.goToMainPage()
                        viewModel.resetState()
                    }

                    is LoginState.Error -> {
                        viewBinding?.progressBar?.visibility = View.GONE
                        viewBinding?.btnLogin?.isEnabled = true
                        Toast.makeText(requireContext(), getString(R.string.fail_login), Toast.LENGTH_SHORT).show()
                        viewModel.resetState()
                    }

                    is LoginState.Initial -> {
                        viewBinding?.progressBar?.visibility = View.GONE
                        viewBinding?.btnLogin?.isEnabled = true
                    }
                }
            }
        }


        viewBinding?.btnLogin?.setOnClickListener {
            val email = viewBinding?.etEmail?.text.toString()
            val password = viewBinding?.etPassword?.text.toString()

            if (email.isNotBlank() && password.isNotBlank()) {
                viewModel.login(email, password)
            } else {
                Toast.makeText(requireContext(), getString(R.string.zapolni), Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewBinding = null
    }
}