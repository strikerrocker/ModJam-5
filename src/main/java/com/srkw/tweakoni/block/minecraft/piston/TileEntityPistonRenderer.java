package com.srkw.tweakoni.block.minecraft.piston;

import com.srkw.tweakoni.init.BlockInit;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPistonExtension.EnumPistonType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static net.minecraft.block.BlockPistonExtension.EnumPistonType.DEFAULT;
import static net.minecraft.block.BlockPistonExtension.EnumPistonType.STICKY;

@SideOnly(Side.CLIENT)
public class TileEntityPistonRenderer extends TileEntitySpecialRenderer<TileEntityPiston> {
    private BlockRendererDispatcher blockRenderer;

    public void render(TileEntityPiston te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        if (blockRenderer == null)
            blockRenderer = Minecraft.getMinecraft().getBlockRendererDispatcher(); //Forge: Delay this from constructor to allow us to change it later
        BlockPos blockpos = te.getPos();
        IBlockState iblockstate = te.getPistonState();
        Block block = iblockstate.getBlock();

        if (iblockstate.getMaterial() != Material.AIR && te.getProgress(partialTicks) < 1.0F) {
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder bufferbuilder = tessellator.getBuffer();
            this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            RenderHelper.disableStandardItemLighting();
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            GlStateManager.enableBlend();
            GlStateManager.disableCull();

            if (Minecraft.isAmbientOcclusionEnabled()) {
                GlStateManager.shadeModel(7425);
            } else {
                GlStateManager.shadeModel(7424);
            }

            bufferbuilder.begin(7, DefaultVertexFormats.BLOCK);
            bufferbuilder.setTranslation(x - (double) blockpos.getX() + (double) te.getOffsetX(partialTicks), y - (double) blockpos.getY() + (double) te.getOffsetY(partialTicks), z - (double) blockpos.getZ() + (double) te.getOffsetZ(partialTicks));
            World world = this.getWorld();

            if (block == BlockInit.PISTON_HEAD && te.getProgress(partialTicks) <= 0.25F) {
                iblockstate = iblockstate.withProperty(BlockPistonExtension.SHORT, Boolean.TRUE);
                this.renderStateModel(blockpos, iblockstate, bufferbuilder, world, true);
            } else if (te.shouldPistonHeadBeRendered() && !te.isExtending()) {
                EnumPistonType blockpistonextension$enumpistontype = block == BlockInit.STICKY_PISTON ? STICKY : DEFAULT;
                IBlockState iblockstate1 = BlockInit.PISTON_HEAD.getDefaultState().withProperty(BlockPistonExtension.TYPE, blockpistonextension$enumpistontype).withProperty(BlockPistonExtension.FACING, iblockstate.getValue(BlockPistonBase.FACING));
                iblockstate1 = iblockstate1.withProperty(BlockPistonExtension.SHORT, te.getProgress(partialTicks) >= 0.5F);
                this.renderStateModel(blockpos, iblockstate1, bufferbuilder, world, true);
                bufferbuilder.setTranslation(x - (double) blockpos.getX(), y - (double) blockpos.getY(), z - (double) blockpos.getZ());
                iblockstate = iblockstate.withProperty(BlockPistonBase.EXTENDED, Boolean.TRUE);
                this.renderStateModel(blockpos, iblockstate, bufferbuilder, world, true);
            } else {
                this.renderStateModel(blockpos, iblockstate, bufferbuilder, world, false);
            }

            bufferbuilder.setTranslation(0.0D, 0.0D, 0.0D);
            tessellator.draw();
            RenderHelper.enableStandardItemLighting();
        }
    }

    private boolean renderStateModel(BlockPos pos, IBlockState state, BufferBuilder buffer, World p_188186_4_, boolean checkSides) {
        return this.blockRenderer.getBlockModelRenderer().renderModel(p_188186_4_, this.blockRenderer.getModelForState(state), state, pos, buffer, checkSides);
    }
}
