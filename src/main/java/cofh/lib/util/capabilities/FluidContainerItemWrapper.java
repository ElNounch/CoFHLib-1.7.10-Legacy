package cofh.lib.util.capabilities;

import cofh.lib.util.helpers.FluidHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidContainerItem;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.FluidTankProperties;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

import javax.annotation.Nullable;

public class FluidContainerItemWrapper implements ICapabilityProvider {

	final ItemStack stack;
	final IFluidContainerItem container;

	public FluidContainerItemWrapper(ItemStack stackIn, IFluidContainerItem containerIn) {

		stack = stackIn;
		container = containerIn;
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing from) {

		return capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, final EnumFacing from) {

		return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(new IFluidHandler() {
			@Override
			public IFluidTankProperties[] getTankProperties() {

				return new IFluidTankProperties[] { new FluidTankProperties(container.getFluid(stack), container.getCapacity(stack), true, false) };
			}

			@Override
			public int fill(FluidStack resource, boolean doFill) {

				return container.fill(stack, resource, doFill);
			}

			@Nullable
			@Override
			public FluidStack drain(FluidStack resource, boolean doDrain) {

				if (FluidHelper.isFluidEqual(resource, container.getFluid(stack))) {
					return container.drain(stack, resource.amount, doDrain);
				}
				return null;
			}

			@Nullable
			@Override
			public FluidStack drain(int maxDrain, boolean doDrain) {

				return container.drain(stack, maxDrain, doDrain);
			}
		});
	}

}
